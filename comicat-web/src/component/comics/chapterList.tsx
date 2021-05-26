import React from 'react';
import {Button, Col, Row} from "antd";

export class ChapterList extends React.Component<any, any> {
    render() {
        return <>
            <Row>
                <Col>章节列表</Col>
                <Col offset={12} onClick={this.props.showImportChapterModal}><Button>导入</Button></Col>
            </Row>
        </>
    }


}
