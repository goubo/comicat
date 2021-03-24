import React from 'react';
import {Layout} from "antd";
import {TagMenu} from "./tag_menu";
import {api} from "../../api";
import {ComicsCard} from "./comics_card";


const {Content, Sider} = Layout

interface queryParams {
    comicsTags: string[]
    tagLogic: string
}

export class BookShelf extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.changeTags = this.changeTags.bind(this);
        this.setTagMap = this.setTagMap.bind(this);
        this.changeTagLogic = this.changeTagLogic.bind(this);
        this.state = {
            tagCode: [],
            tagMap: {},
            list: []
        }
    }

    queryParams: queryParams = {
        comicsTags: [],
        tagLogic: "or",
    }
    setTagMap = (map: any) => this.setState({
        tagMap: map
    });
    changeTagLogic = (tagLogic: string) => {
        this.queryParams.tagLogic = tagLogic
        this.extracted()
    };

    changeTags = (menu: any) => {
        this.setState({
            tagCode: menu
        });
        this.queryParams.comicsTags = menu
        this.extracted()
    };

    private extracted() {
        api.getComicsList(this.queryParams).then(response => {
            if (response && response.data) {
                this.setState({
                    list: response.data.comicsList
                })
            }
        })
    }

    componentDidMount() {
        this.extracted();
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
                    <TagMenu
                        tagLogic={this.queryParams.tagLogic}
                        changeTags={this.changeTags}
                        changeTagLogic={this.changeTagLogic}
                        setTagMap={this.setTagMap}/>
                </Sider>
                <Content>
                    <ComicsCard
                        comicsList={this.state.list}
                        tagList={"tags:" + this.state.tagCode.map((o: string | number) => this.state.tagMap[o] + "")}
                    />
                </Content>
            </Layout>
        )
    }
}
