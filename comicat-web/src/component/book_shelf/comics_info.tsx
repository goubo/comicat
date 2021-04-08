import {Button, Col, Image, Row, Space} from 'antd';
import React from 'react';
import {Constant} from '../../constant';
import {ComTagTag} from '../tags/comtag_tag';

export class ComicsInfo extends React.Component<any, any> {

    editComics = () => {
        this.props.showAddComicsTop()
    }

    render() {
        return (<>
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
                           src={'/api/comics/cover/' + this.props.value.coverImage}/>
                </Col>
                <Col offset={2} span={12}>
                    <Row>书名 : {this.props.value.comicsName}</Row>
                    <Row>作者 : {this.props.value.comicsAuthor}</Row>
                    <Row>描述 : {this.props.value.description}</Row>
                    <Row>标签 : <ComTagTag value={this.props.value.comicsTags}/></Row>
                </Col>
            </Row>
            <Row>
                <Col>章节</Col>
            </Row>
            <Row>
                <Col>章节</Col>
            </Row>
        </>);
    }


}
