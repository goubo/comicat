import React, {useState} from 'react';
import {Button, Card, Col, Row, Space,} from "antd";
import {DeleteOutlined} from '@ant-design/icons';

const cardGridStyle = {width: '25%',}


export const ChapterList = (props: {
    chapterList: any;
    deleteChapter(item: object, index: number): void;
    showImportChapterModal(): void;
}) => {
    const [editChapter, setEditChapter] = useState(false);
    return <>
        <Row gutter={[16, 16]}>
            <Col>章节列表</Col>
            <Col offset={12}><Button size="small" onClick={props.showImportChapterModal}>导入</Button></Col>
            <Col><Button size="small" onClick={() => setEditChapter(!editChapter)}> {editChapter ? "取消" : "修改"}</Button></Col>
        </Row>
        <Card>
            {props.chapterList.map((item: any, idx: any) => (
                <Card.Grid style={cardGridStyle} key={idx}>
                    <Space>
                        {item.chapterName}
                        {editChapter ? <DeleteOutlined onClick={props.deleteChapter.bind(this, item, idx)}/> : null}
                    </Space>
                </Card.Grid>
            ))}
        </Card>
    </>
}