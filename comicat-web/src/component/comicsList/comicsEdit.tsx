import React from 'react';
import {Button, Form, FormInstance, Input, Modal, Select, Space, Upload} from 'antd';
import {UploadOutlined} from "@ant-design/icons";
import TextArea from "antd/lib/input/TextArea";
import {Api} from "../../Api";

const {Option} = Select;


export class ComicsEdit extends React.Component<any, any> {
    private addFormRef = React.createRef<FormInstance>()

    private coverImage: any

    private comicsInfo = {
        comicsTags: '',
        id: '',
    }

    onReset = () => {
        this.addFormRef.current!.resetFields()
    };


    onSubmit = () => {
        let fields = this.addFormRef.current!.getFieldsValue()
        let params = new FormData();
        Object.keys(fields).map((key) => params.set(key, fields[key]))
        params.set('file', this.coverImage)
        if (this.comicsInfo.id) {
            params.set("id", this.props.comicsInfo.id)
            params.set('old', JSON.stringify(this.props.comicsInfo))
            Api.updateComics(params).then(() => {
                this.onReset()
                this.props.closeEditComicsModal()
            })
        } else {
            Api.addComics(params).then(() => {
                this.onReset()
                this.props.closeEditComicsModal()
            })
        }
    }

    beforeUpload = (file: any) => {
        this.coverImage = file
        return false;
    }
    uploadProps = {
        beforeUpload: this.beforeUpload,
        maxCount: 1,
    }

    tagsList2Array = (tagsList: any) => {
        const array: JSX.Element[] = [];
        tagsList.map((s: string) => array.push(<Option key={s} value={s}>{s}</Option>))
        return array
    }

    componentDidMount() {
        this.props.onRef(this)
    }

    loadForm = (comics: any) => {
        this.comicsInfo = comics
        if (comics.id) {
            comics['comicsTagList'] = comics.comicsTags.split(',').filter((s: string) => s)
            this.addFormRef.current!.setFieldsValue(comics)
        } else {
            this.addFormRef.current!.setFieldsValue({
                'comicsName': '',
                'comicsAuthor': '',
                'comicsTags': '',
                'description': '',
                'comicsTagList': [],
            })
        }
    }

    render() {
        return (
            <Modal title={this.props.editComicsTitle} visible={this.props.editComicsVisible}
                   onCancel={this.props.closeEditComicsModal} footer={''} zIndex={21}>
                <Form ref={this.addFormRef} name='addComics' initialValues={this.props.value}>
                    <Form.Item label='书名' name='comicsName'>
                        <Input/>
                    </Form.Item>
                    <Form.Item label='作者' name='comicsAuthor'>
                        <Input/>
                    </Form.Item>
                    <Form.Item label='标签' name='comicsTagList'>
                        <Select mode='tags' allowClear>{this.tagsList2Array(this.props.tagsList)}</Select>
                    </Form.Item>
                    <Form.Item label='封面'>
                        <Upload {...this.uploadProps}>
                            <Button icon={<UploadOutlined/>}>选择封面图片</Button>
                        </Upload>
                    </Form.Item>
                    <Form.Item name='description' label='描述'>
                        <TextArea showCount maxLength={100}/>
                    </Form.Item>
                    <Form.Item>
                        <Space>
                            <Button type='primary' htmlType='submit' onClick={this.onSubmit}>保存</Button>
                            <Button onClick={this.onReset} htmlType='submit'>重置</Button>
                        </Space>
                    </Form.Item>
                </Form>

            </Modal>
        )
    }


}
