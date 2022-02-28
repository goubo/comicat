import hashlib
import io
import os
import urllib.parse
import urllib.request as req
from typing import Optional, List

import requests
from PIL import Image
from fake_useragent import UserAgent
from lxml import etree

from entity import ImageInfo, ChapterInfo, ComicInfo
from mods.website_interface import WebsiteInterface

ua = UserAgent()
rule = [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
hl = hashlib.md5()


class JM18Comicat(WebsiteInterface):

    def __init__(self):
        self.session = requests.Session()
        self.webSiteName = "18comic"
        self.searchUrl = "https://18comic.vip/search/photos?search_query={}&page={}"
        self.domain = "https://18comic.vip"
        self.headers = {
            'cookie': 'ipcountry=US; AVS=gv0mcgslkis91cpfn2lc0pg8q7; shunt=1; _gid=GA1.2.619330605.1645853380; '
                      'ipm5=bfebbeee5f4da93e495d6de300f460ea; cover=1; '
                      '_ga_VW05C6PGN3=GS1.1.1645853380.1.1.1645853704.59; _ga=GA1.2.2032564944.1645853380; _gat_ga0=1; '
                      '_gat_ga1=1',
            'User-Agent': ua.chrome
        }

    def search_callback(self, key, callback) -> List[ComicInfo]:
        comic_info_list: List[ComicInfo] = []
        for page in range(1, 10):
            url = self.searchUrl.format(urllib.parse.quote(key), page)
            request = req.Request(url, headers=self.headers)
            response = req.urlopen(request, timeout=20)
            if response.status != 200:
                print(url, response.status)
            else:
                data = response.read().decode('utf-8')
                tree = etree.HTML(data)
                # urls = tree.xpath("//div[@class='p-b-15 p-l-5 p-r-5']/a/@href")
                # coverUrls = tree.xpath("//div[@class='thumb-overlay']/img/@src")
                # titles = tree.xpath("//span[@class='video-title title-truncate m-t-5']/text()")
                # author_tmp = tree.xpath("//div[@class='title-truncate' or @class='title-truncate hidden-xs']")
                # authors = [da.xpath('string()').replace("\n", "") for da in author_tmp]

                for curi in tree.xpath("//div[@class='p-b-15 p-l-5 p-r-5']/a/@href"):
                    request = req.Request(self.domain + urllib.parse.quote(curi), headers=self.headers)
                    response = req.urlopen(request, timeout=20)
                    data = response.read().decode('utf-8')
                    tree = etree.HTML(data)
                    info = ComicInfo()
                    info.url = self.domain + curi
                    info.coverUrl = tree.xpath("//div[@class='thumb-overlay']/img/@src")[0]
                    info.cover = self.session.get(info.coverUrl, headers=self.headers).content
                    info.domain = self.webSiteName
                    info.title = tree.xpath("//title/text()")[0]
                    info.author = tree.xpath("//span[@data-type='author']")[0].xpath('string()').replace("\n", "")
                    info.describe = tree.xpath('//meta[@name="description"]/@content')[0]
                    info.status = ''
                    info.tip = tree.xpath('//meta[@name="keywords"]/@content')[0]
                    info.heat = tree.xpath(
                        '//*[@id="wrapper"]/div[5]/div[4]/div/div[2]/div[2]/div/div[2]/div[1]/div[10]/span[4]/span/text()')[
                        0].strip()
                    info['updatedAt'] = tree.xpath("//div[@itemprop='datePublished']/@content")[0]
                    self.parse_chapter(info, tree)
                    callback(info)
                    comic_info_list.append(info)

        return comic_info_list

    def chapter_callback(self, comic_info: ComicInfo, callback) -> List[ChapterInfo]:
        if 'chapterList' in comic_info:
            for item in comic_info['chapterList']:
                callback(item)
        else:
            request = req.Request(urllib.parse.quote(comic_info.url), headers=self.headers)
            response = req.urlopen(request, timeout=20)
            if response.status != 200:
                print(comic_info.url, response.status)
            else:
                tree = etree.HTML(response.read().decode('utf-8'))
                self.parse_chapter(comic_info, tree)
        return comic_info['chapterList']

    def parse_chapter(self, comic_info, tree):
        if len(tree.xpath('//div[@class="episode"]')) == 0:
            # 无章节
            chapter_info = ChapterInfo()
            chapter_info.title = comic_info.title
            chapter_info.url = self.domain + \
                               tree.xpath('//a[@class="btn btn-primary dropdown-toggle reading"]/@href')[0]
            chapter_info['updatedAt'] = comic_info['updatedAt']
            comic_info['chapterList'] = [chapter_info]
        else:
            comic_info['chapterList'] = []
            for ca in tree.xpath('//*[@id="wrapper"]/div[5]/div[4]/div/div[2]/div[2]/div/div[2]/div[3]/div/ul/a'):
                chapter_info = ChapterInfo()
                chapter_info.title = ca.xpath('./li/text()')[0].replace("\n", "")
                chapter_info.url = self.domain + ca.attrib.get('href')
                chapter_info['updatedAt'] = comic_info['updatedAt']
                comic_info['chapterList'].append(chapter_info)

    def parse_image_list(self, chapter_info: ChapterInfo) -> List[ImageInfo]:
        request = req.Request(chapter_info.url, headers=self.headers)
        response = req.urlopen(request, timeout=20)
        image_list = []
        if response.status != 200:
            print(chapter_info.url, response.status)
        else:
            tree = etree.HTML(response.read().decode('utf-8'))
            for url in tree.xpath("//img[@class='lazy_img img-responsive-mw']/@data-original"):
                info = ImageInfo()
                info.url = url
                if chapter_info['updatedAt'] < '2020-10-27':
                    info['piece'] = 0
                elif os.path.basename(chapter_info.url) < '268850':
                    info['piece'] = 10
                else:
                    cpp = os.path.basename(chapter_info.url) + os.path.basename(url).split('.')[0]
                    hl.update(cpp.encode(encoding='utf-8'))
                    info['piece'] = rule[ord(hl.hexdigest()[-1]) % 10]

                image_list.append(info)
        return image_list

    def down_image(self, image_info: ImageInfo) -> Optional[bytes]:
        response = self.session.get(image_info.url, headers=self.headers)
        piece = image_info['piece']
        if response.status_code == 200:
            if piece == 0:
                return response.content
            else:
                img = Image.open(io.BytesIO(response.content))
                img_size = img.size
                img_crop_size = int(img_size[1] / piece)
                margin = img_size[1] - img_crop_size * piece
                img_width = int(img_size[0])
                img_block_list = []  # 定义一个列表用来存切割后图片
                for img_count in range(piece):
                    left, upper, right = 0, img_crop_size * img_count, img_width
                    if img_count + 1 == piece:
                        lower = img_crop_size * (img_count + 1)
                    else:
                        lower = img_crop_size * (img_count + 1) + margin
                    img_crop_box = (left, upper, right, lower)
                    img_crop_area = img.crop(img_crop_box)
                    img_block_list.append(img_crop_area)
                img_new = Image.new('RGB', img_size)
                count = 0
                for img_block in reversed(img_block_list):
                    img_new.paste(img_block, (0, count * img_crop_size))
                    count += 1
                buf = io.BytesIO()
                img_new.save(buf, format='jpeg')
                return buf.getvalue()
        else:
            return None


if __name__ == '__main__':
    j = JM18Comicat()
    i = ImageInfo()
    i.url = 'https://cdn-msp.18comic.vip/media/photos/317925/00003.jpg?v=1645859295'
    i['piece'] = rule[ord('860265e95b9e3b169d1d60f0b48a0d36'[-1]) % 10]
    image_byte = j.down_image(i)
    with open('/Users/bo/Downloads/comicat_down/test.jpg', "wb") as f:
        f.write(image_byte)
