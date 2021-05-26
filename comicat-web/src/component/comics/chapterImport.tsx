import {Button, Col, Divider, Form, FormInstance, Input, InputNumber, message, Modal, Radio, Row, Upload} from 'antd';
import React from 'react';
import {UploadOutlined} from "@ant-design/icons";
import {Api} from "../../Api";

export class ChapterImport extends React.Component<any, any> {
    private formRef = React.createRef<FormInstance>()

    constructor(props: any) {
        super(props);
        this.state = {
            fileUploadDone: true,
            chapterInfo: {
                chapterType: "连载"
            },
        }
    }

    handleChange = (info: any) => {
        if (info.file.status === 'done') {
            console.log("上传成功。")
            this.setState({fileUploadDone: false, chapterInfo: info.file.response})
        } else if (info.file.status === 'error') {
            console.log("上传失败。")
            message.error(info.file.response).then(r => console.log(r))
            console.log(info)
        }
    }
    uploadProps = {
        maxCount: 1,
        accept: ".zip,.pdf,.mobi,.epub",
        action: "/api/chapter/upload",
        onChange: this.handleChange
    }
    onSubmit = () => {
        let fields = this.formRef.current!.getFieldsValue()
        fields.comicsId = this.props.comicsInfo.id
        fields.status = 1
        fields.pageNumber = this.state.chapterInfo.pageNumber
        fields.uploadPath = this.state.chapterInfo.uploadPath
        fields.comics = this.props.comicsInfo
        Api.addChapter(fields).then(r => {
            console.log(r)
        }).finally(() => {
            console.log('finally')
        })
    }


    showFileInfo = (b: boolean) => {
        if (!b)
            return <div>
                <Divider/>
                <Row>
                    <Col>页数</Col>
                    <Col>{this.state.chapterInfo.pageNumber}</Col>
                </Row>
            </div>
    }

    render() {
        return <>
            <Modal visible={this.props.importChapterVisible} title={"导入章节"} zIndex={10}
                   onCancel={this.props.closeImportChapterModal} footer={''}>
                漫画名称 : {this.props.comicsInfo.comicsName}
                <Divider/>
                选择文件 :
                <Upload {...this.uploadProps} data={this.props.comicsInfo}>
                    <Button icon={<UploadOutlined/>}>选择章节</Button>
                </Upload>
                {this.showFileInfo(this.state.fileUploadDone)}
                <Divider/>
                <Form ref={this.formRef} name='addComics' initialValues={this.state.chapterInfo}>
                    <Form.Item label='章节名称' name='chapterName'>
                        <Input/>
                    </Form.Item>
                    <Form.Item label='章节名称' name='chapterType'>
                        <Radio.Group>
                            <Radio.Button value="连载">连载</Radio.Button>
                            <Radio.Button value="番外">番外</Radio.Button>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label='排序索引' name='chapterIndex'>
                        <InputNumber/>
                    </Form.Item>
                    <Form.Item>
                        <Button disabled={this.state.fileUploadDone} type='primary' htmlType='submit'
                                onClick={this.onSubmit}>保存</Button>
                    </Form.Item>
                </Form>
            </Modal>
        </>;
    }

}
