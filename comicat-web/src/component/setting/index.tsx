import React from 'react';
import {Button, Form, FormInstance, Input, Space, Switch} from 'antd';
import {Api} from '../../Api';

const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 8},
};
const tailLayout = {
    wrapperCol: {offset: 8, span: 8},
};

export class Setting extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            config: {},
            proxyEnable: false,
        }
    }

    formRef = React.createRef<FormInstance>()
    onReset = () => {
        this.formRef.current!.resetFields()
    };

    onSubmit = () => {
        console.log(this.formRef.current!.getFieldsValue())
    }

    componentDidMount() {
        Api.getConfig().then(response => {
            if (response && response.data) {
                this.setState({
                    config: response.data,
                    proxyEnable: response.data.proxy.enable
                })
                this.formRef.current!.setFieldsValue(response.data)
            }
        })
    }

    proxyEnableChange = (e: boolean) => {
        this.setState({
            proxyEnable: e
        })
    }

    render() {

        return (<div>

                <Form {...layout} ref={this.formRef}
                      name='setting'
                >
                    <Form.Item
                        label='文件保存路径'
                        name='basePath'>
                        <Input/>
                    </Form.Item>
                    <Form.Item label='网络代理' name={['proxy', 'enable']} valuePropName='checked'>
                        <Switch onChange={this.proxyEnableChange}/>
                    </Form.Item>
                    {proxyItem(this.state.proxyEnable)}
                    <Form.Item {...tailLayout}>
                        <Space>
                            <Button type='primary' htmlType='submit' onClick={this.onSubmit}>保存</Button>
                            <Button onClick={this.onReset} htmlType='submit'><span/>
                                Reset
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </div>
        )
     }
}

function proxyItem(proxyEnable: boolean) {
    if (proxyEnable)
        return (<div>

            <Form.Item
                label='代理服务器ip'
                name='socket url'>
                <Input/>
            </Form.Item>
        </div>)
    else return ''


}
