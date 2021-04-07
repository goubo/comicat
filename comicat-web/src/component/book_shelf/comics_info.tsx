import {Col, Image, Row, Tag} from "antd";
import React from "react";
import {Constant} from "../../constant";

export class ComicsInfo extends React.Component<any, any> {
    tags2List = (tag: string) => {
        return tag.split(',').filter(s => s).map(s => {
            return <Tag style={{marginBottom: '1vh'}}>{s}</Tag>
        })
    }


    render() {
        return (<>
            <Row>
                <Col span={10}>
                    < Image alt='example'
                            preview={false}
                            fallback={Constant.default_image}
                            src={'/api/comics/cover/' + this.props.value.coverImage}/>
                </Col>
                <Col offset={2} span={12}>
                    <Row>书名 : {this.props.value.comicsName}</Row>
                    <Row>作者 : {this.props.value.comicsAuthor}</Row>
                    <Row>描述 : {this.props.value.description}</Row>
                    <Row>标签 : {this.tags2List(this.props.value.comicsTags)}</Row>
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
