import React from 'react';
import {Layout} from "antd";
import {TagMenu} from "./tag_menu";
import {BookView} from './book_view';


const {Content, Sider} = Layout


export class BookShelf extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.changeMenu = this.changeMenu.bind(this);
        this.state = {
            tagCode: ''
        }
    }

    changeMenu(menu: string) {
        this.setState({
            tagCode: menu
        })
    }

    render() {
        return (
            <Layout>
                <Sider breakpoint="lg"
                       collapsedWidth="0"
                       theme={"light"}
                       style={{
                           overflow: 'auto',
                           height: '90vh',
                           left: 0,
                       }}>
                    <TagMenu changeMenu={this.changeMenu}/>
                </Sider>
                <Content>
                    <BookView
                        tagCode={this.state.tagCode}/>
                </Content>
            </Layout>
        )
    }
}
