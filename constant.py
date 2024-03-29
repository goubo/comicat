import os.path
import shelve

from service import Service
# 程序退出状态,线程监测此状态后,程序处于等待退出状态
from util import find_database_access_class

APPLICATION_EXIT = False

# 打开的tab页集合,记录页面的url用于区分
OPEN_TAB = []

# 全局业务主线程
SERVICE = Service()

# 加载的所有mod 保存mod对象

mod_dist = dict()
for class_name, class_ in find_database_access_class("comicat", "mods").items():
    co = class_()
    mod_dist[class_name] = co

# 配置

# 本地数据
download_task_widget_map = dict()

downloaded_chapter_map = dict()
'''下载完成的章节列表, 与 downloaded_task_map相同,当没网的时候用于加载本地数据'''
downloaded_comic_map = dict()

# 临时数据
temp = dict()
'''查询缓存,mod解析的页面返回,统统缓存到这个对象中
k: url
v:object
'''
file_path = os.path.join(os.path.expanduser('~'), '.cache', 'comicat')
if not os.path.exists(file_path):
    os.makedirs(file_path)
DB = shelve.open(os.path.join(file_path, 'userDB'))
# 下载历史,用于确认本地数据是否存在
downloaded_task_map = DB.get('downloaded_task_map', dict())
# 下载队列.key:value ,url:taskInfo,下载完成后删除
download_task_map = DB.get('download_task_map', dict())
# 书架列表
downloaded_comic_map = DB.get('downloaded_comic_map', dict())
mod_list = DB.get('mod_list') if DB.get('mod_list') else set()
'''启用的mod列表'''

# 配置

down_path = os.path.join(os.path.expanduser('~'), 'Downloads', 'comicat_down')
