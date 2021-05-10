import React from 'react';
import {Layout} from 'antd';
import {ComicsSider} from './comicsSider';
import {Api} from '../../Api';
import {ComicsContent} from './comicsContent';
import {ComicsEdit} from "./comicsEdit";
import {ComicsInfo} from './comicsInfo';


const {Content, Sider} = Layout

interface QueryParams {
    comicsTags: string[]
    tagLogic: string
}

interface ComicsInfoModal {
    visible: boolean
    title: string
    info: object
}

export class ComicsList extends React.Component<any, any> {
    private comicsEditRef: any;

    constructor(props: any) {
        super(props);
        this.changeQueryParams = this.changeQueryParams.bind(this);
        this.showEditComicsModal = this.showEditComicsModal.bind(this);
        this.showComicsInfoModal = this.showComicsInfoModal.bind(this);
        this.state = {
            comicsList: [],
            tagsList: [],
            editComicsTitle: "",
            editComicsVisible: false,
            comicsInfoVisible: false,
            comicsInfo: {},
            queryParams: {
                comicsTags: [],
                tagLogic: 'or',
            }
        }
    }

    onComicsEditRef = (ref: any) => {
        this.comicsEditRef = ref
    }

    /**
     * 编辑漫画窗口
     * @param comicsInfo
     */
    showEditComicsModal = (comicsInfo: ComicsInfoModal) => {
        this.setState({
            editComicsVisible: comicsInfo.visible,
            editComicsTitle: comicsInfo.title,
            comicsInfo: comicsInfo.info
        }, () => this.comicsEditRef.loadForm(comicsInfo.info))
    }
    closeEditComicsModal = () => {
        this.setState({
            editComicsVisible: false,
        })
        this.getComicsList()

    }
    /**
     *
     */
    showComicsInfoModal = (comicsInfo: ComicsInfoModal) => {
        this.setState({
            comicsInfoVisible: true,
            comicsInfo: comicsInfo.info
        })
    }
    closeComicsInfoModal = () => {
        this.setState({
            comicsInfoVisible: false,
        })
    }
    //左侧修改查询条件
    changeQueryParams = (queryParams: QueryParams) => {
        this.setState({
            queryParams: queryParams
        }, () => this.getComicsList())
    }


    componentDidMount() {
        this.getComicsList();
        Api.getTagList({}).then(response => {
            if (response && response.data) {
                this.setState({tagsList: response.data})
            }
        })
    }

    private getComicsList() {
        Api.getComicsList(this.state.queryParams).then(response => {
            if (response && response.data) {
                this.setState({
                    comicsList: response.data.comicsList
                })
                if (this.state.comicsInfo.id) {
                    response.data.comicsList.forEach((c: { id: any; }) => {
                        if (c.id === this.state.comicsInfo.id) {
                            this.setState({comicsInfo: c})
                        }
                    })
                }
            }
        })
    }

    render() {
        return <>
            <Layout>
                <Sider breakpoint='lg' collapsedWidth='0' theme={'light'}
                       style={{overflow: 'auto', height: '90vh', left: 0,}}>
                    <ComicsSider tagLogic={this.state.queryParams.tagLogic} tagsList={this.state.tagsList}
                                 changeQueryParams={this.changeQueryParams}/>
                </Sider>
                <Content>
                    <ComicsContent comicsList={this.state.comicsList} tagCode={this.state.queryParams.comicsTags}
                                   tagsList={this.state.tagsList} showEditComicsModal={this.showEditComicsModal}
                                   showComicsInfoModal={this.showComicsInfoModal}/>
                </Content>
            </Layout>
            <ComicsEdit {...this.state} closeEditComicsModal={this.closeEditComicsModal} onRef={this.onComicsEditRef}
                        showEditComicsModal={this.showEditComicsModal}/>
            <ComicsInfo {...this.state} closeComicsInfoModal={this.closeComicsInfoModal}
                        showEditComicsModal={this.showEditComicsModal}/>

        </>

    }


}
