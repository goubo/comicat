import {Button, Col, Image, Modal, Row, Space} from 'antd';
import React from 'react';
import {Constant} from '../../constant';
import {ComTagTag} from '../tags/comtag_tag';

export class ComicsInfo extends React.Component<any, any> {

    editComics = () => this.props.showEditComicsModal(
        {title: "修改描述", visible: true, info: this.props.comicsInfo})

    render() {
        return (<>
            <Modal visible={this.props.comicsInfoVisible} title={"漫画详情"} zIndex={10}
                   onCancel={this.props.closeComicsInfoModal} footer={''}>
                <Row>
                    <Col>
                        <Space>
                            <Button size='small' onClick={this.editComics}>修改描述</Button>
                            <Button size='small'>导入章节</Button>
                        </Space>
                    </Col>
                </Row>
                <Row>
                    <Col span={10}>
                        <Image alt='example' preview={false} fallback={Constant.default_image}
                               src={'/api/comics/cover/' + this.props.comicsInfo.coverImage}/>
                    </Col>
                    <Col offset={2} span={12}>
                        <Row>书名 : {this.props.comicsInfo.comicsName}</Row>
                        <Row>作者 : {this.props.comicsInfo.comicsAuthor}</Row>
                        <Row>描述 : {this.props.comicsInfo.description}</Row>
                        <Row>标签 : <ComTagTag value={this.props.comicsInfo.comicsTags}/></Row>
                    </Col>
                </Row>
                <Row>
                    <Col>章节</Col>
                </Row>
                <Row>
                    <Col>章节</Col>
                </Row>
            </Modal>
        </>);
    }


}
