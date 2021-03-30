import React from 'react';
import {Form, Input, Select} from "antd";

export class AddComics extends React.Component<any, any> {
    render() {
        return (
            <Form>
                <Form.Item label="书名" name="comicsName">
                    <Input/>
                </Form.Item>
                <Form.Item label="作者" name="comicsAuthor">
                    <Input/>
                </Form.Item>
                <Form.Item label="标签" name="comicsAuthor">
                    <Select mode="tags">

                    </Select>

                </Form.Item>
            </Form>
        )
    }
}
