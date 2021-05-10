import React from 'react';
import {Button, Card, Col, Image, Radio, Row, Space, Tooltip} from 'antd';
import {PlusCircleOutlined} from '@ant-design/icons';
import {Constant} from '../../constant';
import {ComTagTag} from '../tags/comtag_tag';

const {Meta} = Card;

export class ComicsContent extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            addComicsTop: false,
            showComicsInfoTop: false,
            comicsInfo: {},
        }
    }

    showComicsInfo = (item: object) => this.props.showComicsInfoModal({info: item})
    showAddComics = () => this.props.showEditComicsModal({visible: true, title: "新增漫画", info: {}})

    render() {
        return (
            <div>
                <Row style={{padding: '2vh'}}>
                    <Col offset={1} span={18}>
                        tags : <ComTagTag value={this.props.tagCode}/>
                    </Col>
                    <Col>
                        <Space>
                            <Tooltip title='添加'>
                                <Button type='primary' shape='circle' icon={<PlusCircleOutlined/>}
                                        onClick={this.showAddComics}/>
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
                            {this.props.comicsList.map((item: any, index: number) => {
                                return (
                                    <Card key={index} hoverable style={{whiteSpace: 'pre-wrap'}}
                                          onClick={this.showComicsInfo.bind(this, item)}>
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
}
