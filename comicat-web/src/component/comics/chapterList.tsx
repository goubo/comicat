import React from 'react';
import {Button, Col, List, Row,} from "antd";

export class ChapterList extends React.Component<any, any> {
    render() {
        return <>
            <Row>
                <Col>章节列表</Col>
                <Col offset={12} onClick={this.props.showImportChapterModal}><Button>导入</Button></Col>
            </Row>
            <Row>
                {JSON.stringify(this.props.chapterList)}
                <List grid={{
                    gutter: 16, //栅格间隔
                    xs: 4, //展示的列数
                    sm: 4, // 展示的列数
                    md: 4,//展示的列数
                    lg: 4,//展示的列数
                    xl: 6,//展示的列数
                    xxl: 6,//展示的列数
                }}>

                </List>
            </Row>
        </>
    }


}
