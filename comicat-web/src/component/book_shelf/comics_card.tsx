import React from 'react';
import {Button, Card, Col, Modal, Radio, Row, Space, Tooltip} from 'antd';
import {PlusCircleOutlined} from '@ant-design/icons';
import {AddComics} from './add_comics';

const {Meta} = Card;

export class ComicsCard extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            comicsList: this.props.comicsList,
            tagCode: this.props.tagCode,
            addComicsTop: false,
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
    showAddComicsTop = () => {
        this.setState({
            addComicsTop: true,
        })
    }
    closeAddComicsTop = () => {
        this.setState({
            addComicsTop: false,
        })
    }

    render() {

        return (
            <div>
                <Row style={{paddingTop: '2vh'}}>
                    <Col offset={1} span={20}> {this.state.tagCode}</Col>
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
                <Space wrap align={'baseline'}>
                    {this.state.comicsList.map((item: any, index: number) => {
                        return (
                            <Card key={index} hoverable>
                                <img
                                    width={200}
                                    alt='example'
                                    src={'/api/comics/cover/' + item.coverImage}
                                />

                                <Meta
                                    title={item.comicsName}
                                    description={item.description}
                                />
                            </Card>
                        )
                    })}
                </Space>

                <Modal title='添加漫画' visible={this.state.addComicsTop} onCancel={this.closeAddComicsTop}
                       footer={''}>
                    <AddComics tagsList={this.props.tagsList} close={this.closeAddComicsTop}/>
                </Modal>
            </div>)
    }


}
