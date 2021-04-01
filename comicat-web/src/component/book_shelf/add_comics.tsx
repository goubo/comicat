import React from 'react';
import {Button, Form, FormInstance, Input, Select, Space, Upload} from "antd";
import {Api} from "../../Api";
import {UploadOutlined} from '@ant-design/icons';

const {Option} = Select;

export class AddComics extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            coverImage: []
        }
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
        console.log(this.addFormRef.current!.getFieldsValue())
        Api.addComics(this.addFormRef.current!.getFieldsValue()).then(() => {
            this.props.close()
        })
    }

    beforeUpload = (file: any) => {
        this.setState({
            coverImage: [file],
        });
        return false;
    }

    uploadProps = {
        beforeUpload: this.beforeUpload,
        maxCount: 1
    }

    render() {

        return (
            <Form ref={this.addFormRef} name="addComics">
                {this.state.coverImage}
                <Form.Item label="书名" name="comicsName">
                    <Input/>
                </Form.Item>
                <Form.Item label="作者" name="comicsAuthor">
                    <Input/>
                </Form.Item>
                <Form.Item label="标签" name="comicsTagList">
                    <Select mode="tags" allowClear>{this.tagsList2Array(this.props.tagsList)}</Select>
                </Form.Item>
                <Upload {...this.uploadProps}>
                    <Button icon={<UploadOutlined/>}>Upload png only</Button>
                </Upload>

                <Form.Item>
                    <Space>
                        <Button type="primary" htmlType="submit" onClick={this.onSubmit}>保存</Button>
                        <Button onClick={this.onReset} htmlType="submit"><span/>
                            Reset
                        </Button>
                    </Space>
                </Form.Item>
            </Form>
        )
    }


}
