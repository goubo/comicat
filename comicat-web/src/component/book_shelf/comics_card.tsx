import React from 'react';
import {Button, Card, Col, Image, Modal, Radio, Row, Space, Tooltip} from 'antd';
import {PlusCircleOutlined} from '@ant-design/icons';
import {ComicsEdit} from './comics_edit';
import {Constant} from '../../constant';
import {ComicsInfo} from "./comics_info";
import {ComTagTag} from "../tags/comtag_tag";

const {Meta} = Card;

export class ComicsCard extends React.Component<any, any> {
    private comicsEditRef: any;


    constructor(props: any) {
        super(props);
        this.state = {
            comicsList: this.props.comicsList,
            tagCode: this.props.tagCode,
            addComicsTop: false,
            showComicsInfoTop: false,
            comicsInfo: {},
        }
    }

    static getDerivedStateFromProps(props: any) {
        return {
            tagCode: props.tagCode,
            comicsList: props.comicsList,
        }
    }

    handleSizeChange = (e: any) => {
        console.log(e.target.value)
    }
    showAddComicsTop = (source: any) => {
        this.setState({
            addComicsTop: true,
        })
        if (!source) {
            if (this.comicsEditRef)
                this.comicsEditRef.loadForm(this.state.comicsInfo)
        } else {
            if (this.comicsEditRef)
                this.comicsEditRef.loadForm(undefined)
        }

    }
    clickComics = (item: object) => {
        this.setState({
            comicsInfo: item,
            showComicsInfoTop: true,
        })
    }
    closeAddComicsTop = () => {
        this.setState({
            addComicsTop: false,
        })
    }
    closeComicsInfoTop = () => {
        this.setState({
            showComicsInfoTop: false
        })
    }

    onComicsEditRef = (ref: any) => {
        this.comicsEditRef = ref
    }

    render() {

        return (
            <div>
                <Row style={{padding: '2vh'}}>
                    <Col offset={1} span={18}>
                        tags:<ComTagTag value={this.state.tagCode}/>
                    </Col>
                    <Col>
                        <Space>
                            <Tooltip title='添加'>
                                <Button type='primary' shape='circle' icon={<PlusCircleOutlined/>}
                                        onClick={this.showAddComicsTop}/>
                            </Tooltip>
                            <Radio.Group onChange={this.handleSizeChange} defaultValue={'table'}>
                                <Radio.Button value='table'>网格</Radio.Button>
                                <Radio.Button value='list'>列表</Radio.Button>
                            </Radio.Group>
                        </Space>
                    </Col>
                </Row>
                <Row style={{padding: '2vh'}}>
                    <Col>
                        <Space wrap align={'baseline'}>
                            {this.state.comicsList.map((item: any, index: number) => {
                                return (
                                    <Card key={index} hoverable style={{whiteSpace: 'pre-wrap'}}
                                          onClick={this.clickComics.bind(this, item)}>
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
                <Modal title='添加漫画' visible={this.state.addComicsTop} style={{zIndex: 20}}
                       onCancel={this.closeAddComicsTop} footer={''}>
                    <ComicsEdit onRef={this.onComicsEditRef} value={this.state.comicsInfo} tagsList={this.props.tagsList}
                                close={this.closeAddComicsTop}/>
                </Modal>
                <Modal title='漫画详情' visible={this.state.showComicsInfoTop} style={{zIndex: 10}}
                       onCancel={this.closeComicsInfoTop} footer={''}>
                    <ComicsInfo value={this.state.comicsInfo}
                                showAddComicsTop={this.showAddComicsTop}
                                close={this.closeComicsInfoTop}/>
                </Modal>
            </div>)
    }
}
