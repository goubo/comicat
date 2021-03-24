import React from 'react'
import {Col, Radio, Row} from 'antd'
import {api} from "../../api";
import {ComicsCard} from "./comics_card";


export class BookView extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            list: []
        }
    }

    handleSizeChange = (e: any) => {
        console.log(e.target.value)
    }

    componentWillReceiveProps(newProps: any) {
        this.extracted(newProps);
    }

    private extracted(props: any) {
        api.getComicsList({
            comicsTags: props.tagCode
        }).then(response => {
            if (response && response.data) {
                this.setState({
                    list: response.data.comicsList
                })
            }
        })
    }

    componentDidMount() {
        this.extracted(this.props);
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
                <ComicsCard comicsList={this.state.list} tagList={this.props.tagCode}/>

            </div>
        );
    }
}
