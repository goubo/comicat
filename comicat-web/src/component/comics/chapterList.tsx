import React from 'react';
import {Button, Card, Col, Row, Space,} from "antd";
import {DeleteOutlined} from '@ant-design/icons';


export class ChapterList extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            editChapter: false
        }
    }

    cardGridStyle = {width: '25%',}

    render() {
        return <>
            <Row gutter={[16, 16]}>
                <Col>章节列表</Col>
                <Col offset={12}><Button size="small" onClick={this.props.showImportChapterModal}>导入</Button></Col>
                <Col ><Button size="small">修改</Button></Col>
            </Row>
            <Card>
                {this.props.chapterList.map((item: any, idx: any) => (
                    <Card.Grid style={this.cardGridStyle} key={idx}>
                        <Space>
                            {item.chapterName}
                            {this.state.editChapter ? <DeleteOutlined/> : null}
                        </Space>
                    </Card.Grid>
                ))}
            </Card>
        </>
    }


}
