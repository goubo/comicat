import {Button, Col, Image, Modal, Row, Space} from 'antd';
import React from 'react';
import {Constant} from '../../constant';
import {ComTagTag} from '../tags/comtag_tag';
import {ChapterList} from "./chapterList";

export interface ComicsInfoModal {
    visible: boolean
    title: string
    info: any
}

export const ComicsInfo = (props: {
    showImportChapterModal(): void,
    closeComicsInfoModal(): void,
    showEditComicsModal(comicsInfo: ComicsInfoModal): any,
    comicsInfo: any,
    comicsInfoVisible: any,
}) => {
    const editComics = () => props.showEditComicsModal(
        {title: "修改描述", visible: true, info: props.comicsInfo})

    return <Modal visible={props.comicsInfoVisible} title={"漫画详情"} zIndex={10}
                  onCancel={props.closeComicsInfoModal} footer={''}>
        <Row>
            <Col>
                <Space>
                    <Button size='small' onClick={editComics}>修改描述</Button>
                </Space>
            </Col>
        </Row>
        <Row>
            <Col span={10}>
                <Image alt='example' preview={false} fallback={Constant.default_image}
                       src={'/api/comics/cover/' + props.comicsInfo.coverImage}/>
            </Col>
            <Col offset={2} span={12}>
                <Row>书名 : {props.comicsInfo.comicsName}</Row>
                <Row>作者 : {props.comicsInfo.comicsAuthor}</Row>
                <Row>描述 : {props.comicsInfo.description}</Row>
                <Row>标签 : <ComTagTag value={props.comicsInfo.comicsTags}/></Row>
            </Col>
        </Row>
        <ChapterList showImportChapterModal={props.showImportChapterModal}
                     chapterList={props.comicsInfo.chapterList}
        />
    </Modal>;
}