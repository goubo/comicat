import React from 'react';
import {Card} from "antd";

export class ComicsCard extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            comicsList: this.props.comicsList,
            tagList: this.props.tagList
        }
    }

    componentWillReceiveProps(newProps: any) {
        this.setState({
            tagList: newProps.tagList
        })
    }

    render() {
        return <Card title={this.state.tagList}>
            <Card.Grid>Content</Card.Grid>
        </Card>
    }
}
