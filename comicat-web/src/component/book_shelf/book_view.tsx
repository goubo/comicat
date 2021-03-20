import React from 'react';
import {Col, Radio, Row} from 'antd';


export class BookView extends React.Component<any, any> {
    handleSizeChange = (e: any) => {
        console.log(e.target.value)
    }

    render() {
        return (
                <div>
                    <Row style={{paddingTop: '2vh'}}>
                        <Col offset={21}>
                            <Radio.Group onChange={this.handleSizeChange} defaultValue={'table'}>
                                <Radio.Button value="table">网格</Radio.Button>
                                <Radio.Button value="list">列表</Radio.Button>
                            </Radio.Group>
                        </Col>
                    </Row>
                    tagCode {this.props.tagCode}
                </div>
        );
    }
}
