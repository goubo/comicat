import React from 'react';
import {Button, Form, FormInstance, Input, Space} from "antd";

const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 8},
};
const tailLayout = {
    wrapperCol: {offset: 8, span: 8},
};

export class Setting extends React.Component<any, any> {

    formRef = React.createRef<FormInstance>()
    onReset = () => {
        this.formRef.current!.resetFields()
    };

    onSubmit = () => {
        this.formRef.current!.getFieldsValue()
    }


    render() {
        return (
            <Form {...layout} ref={this.formRef}
                  name="setting"
            >
                <Form.Item
                    label="文件路径"
                    name="basePath"
                >
                    <Input/>
                </Form.Item>
                <Form.Item {...tailLayout}>
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
