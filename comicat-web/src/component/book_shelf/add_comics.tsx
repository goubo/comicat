import React from 'react';
import {Button, Form, FormInstance, Input, Select, Space, Upload} from 'antd';
import {Api} from '../../Api';
import {UploadOutlined} from '@ant-design/icons';
import TextArea from 'antd/lib/input/TextArea';

const {Option} = Select;

export class AddComics extends React.Component<any, any> {

    coverImage: any

    constructor(props: any) {
        super(props);
        this.state = {}
    }

    addFormRef = React.createRef<FormInstance>()
    tagsList2Array = (tagsList: any) => {
        const array: JSX.Element[] = [];
        tagsList.map((s: string) => array.push(<Option key={s} value={s}>{s}</Option>))
        return array
    }

    onReset = () => {
        this.addFormRef.current!.resetFields()
    };

    onSubmit = () => {
        let fields = this.addFormRef.current!.getFieldsValue()
        let params = new FormData();
        Object.keys(fields).map((key) => params.set(key, fields[key]))
        params.set("file", this.coverImage)
        Api.addComics(params).then(() => {
            this.onReset()
            this.props.close()
        })
    }

    beforeUpload = (file: any) => {
        this.coverImage = file
        console.log(file)
        return false;
    }

    uploadProps = {
        beforeUpload: this.beforeUpload,
        maxCount: 1
    }

    render() {

        return (
            <Form ref={this.addFormRef} name='addComics'>
                {this.state.coverImage}
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
                        <Button icon={<UploadOutlined/>}>Upload png only</Button>
                    </Upload>
                </Form.Item>
                <Form.Item name='coverImage' label='描述'>
                    <TextArea showCount maxLength={100}/>
                </Form.Item>

                <Form.Item>
                    <Space>
                        <Button type='primary' htmlType='submit' onClick={this.onSubmit}>保存</Button>
                        <Button onClick={this.onReset} htmlType='submit'><span/>
                            Reset
                        </Button>
                    </Space>
                </Form.Item>
            </Form>
        )
    }


}
