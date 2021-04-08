import React from 'react';
import {Button, Form, FormInstance, Input, InputNumber, message, Popover, Space, Switch} from 'antd';
import {Api} from '../../Api';

const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 8},
};
const tailLayout = {
    wrapperCol: {offset: 8, span: 8},
};

export class Setting extends React.Component<any, any> {

    formRef = React.createRef<FormInstance>()

    constructor(props: any) {
        super(props);
        this.state = {
            config: {},
            proxyEnable: false,
        }
    }

    onReset = () => {
        this.formRef.current!.resetFields()
    };

    onSubmit = () => {
        Api.setConfig(this.formRef.current!.getFieldsValue()).then(() => {
            message.success('保存成功').then(null)
        })
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
                    <Form.Item label='文件保存路径' name='basePath'>
                        <Input/>
                    </Form.Item>
                    <Popover content='9至81' title='每页显示漫画数量'>
                        <Form.Item label='每页显示漫画数量' name='pageSize'>
                            <InputNumber min={9} max={81} keyboard/>
                        </Form.Item>
                    </Popover>
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
