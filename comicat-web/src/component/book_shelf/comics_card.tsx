import React from 'react';
import {Card, Col, Radio, Row, Space} from "antd";

const {Meta} = Card;

export class ComicsCard extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            comicsList: this.props.comicsList,
            tagList: this.props.tagList
        }
    }

    static getDerivedStateFromProps(props: any, state: any) {
        return {
            tagList: props.tagList,
            comicsList: props.comicsList,
        }
    }

    handleSizeChange = (e: any) => {
        console.log(e.target.value)
    }

    render() {
        return (
            <div>
                <Row style={{paddingTop: '2vh'}}>
                    <Col offset={1} span={20}> {this.state.tagList}</Col>
                    <Col>
                        <Radio.Group onChange={this.handleSizeChange} defaultValue={'table'}>
                            <Radio.Button value="table">网格</Radio.Button>
                            <Radio.Button value="list">列表</Radio.Button>
                        </Radio.Group>
                    </Col>
                </Row>
                <Space wrap align={"center"}>
                    {this.state.comicsList.map((item: any, index: number) => {
                        return (
                            <Card key={index}>
                                <img
                                    alt="example"
                                    src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
                                />
                                <Meta
                                    title={item.comicsName}
                                    description=""
                                />
                            </Card>
                        )
                    })}
                </Space>
            </div>)
    }
}
