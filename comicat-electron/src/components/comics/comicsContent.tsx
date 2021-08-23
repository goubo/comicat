import React from 'react';
import {Button, Card, Col, Image, Radio, Row, Space, Tooltip} from 'antd';
import {PlusCircleOutlined} from '@ant-design/icons';
import {Constant} from '../../constant';
import {ComTagTag} from '../tags/comtag_tag';

const {Meta} = Card;


export const ComicsContent = (props: {
    tagCode: string,
    tagsList: [],
    comicsList: [],
    showEditComicsModal(param: { visible: boolean; title: string; info: {} }): any,
    showComicsInfoModal(param: { info: object }): any
}) => {
    const showComicsInfo = (item: object) => props.showComicsInfoModal({info: item})
    const showAddComics = () => props.showEditComicsModal({visible: true, title: "新增漫画", info: {}})
    return (<div>
        <Row style={{padding: '2vh'}}>
            <Col offset={1} span={18}>
                tags : <ComTagTag value={props.tagCode}/>
            </Col>
            <Col>
                <Space>
                    <Tooltip title='添加'>
                        <Button type='primary' shape='circle' icon={<PlusCircleOutlined/>}
                                onClick={showAddComics}/>
                    </Tooltip>
                    <Radio.Group defaultValue={'table'}>
                        <Radio.Button value='table'>网格</Radio.Button>
                        <Radio.Button value='list'>列表</Radio.Button>
                    </Radio.Group>
                </Space>
            </Col>
        </Row>
        <Row style={{padding: '2vh'}}>
            <Col>
                <Space wrap align={'baseline'}>
                    {props.comicsList.map((item: any, index: number) => {
                        return (
                            <Card key={index} hoverable style={{whiteSpace: 'pre-wrap'}}
                                  onClick={showComicsInfo.bind(this, item)}>
                                <Image width={200} alt='example' preview={false}
                                       fallback={Constant.default_image}
                                       src={'/api/comics/cover/' + item.coverImage}/>
                                <Meta title={item.comicsName} description={item.comicsAuthor}/>
                            </Card>
                        )
                    })}
                </Space>
            </Col>
        </Row>
    </div>)
}