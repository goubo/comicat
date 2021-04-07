import React from 'react';
import {Tabs} from 'antd';
import './App.css';
import {BookShelf} from './component/book_shelf';
import TestBOBO from './component/test/Test';
import {Setting} from './component/setting';

const {TabPane} = Tabs;

function App() {
    return (
        <div>
            <Tabs defaultActiveKey='2' centered style={{padding: '0 5vh'}}>
                <TabPane tab='搜索' key='1'>
                    Content of Tab Pane 1
                </TabPane>
                <TabPane tab='书架' key='2'>
                    <BookShelf/>
                </TabPane>
                <TabPane tab='下载' key='3'>
                    Content of Tab Pane 3
                </TabPane>
                <TabPane tab='设置' key='4'>
                    <Setting/>
                </TabPane>
                <TabPane tab='测试' key='5'>
                    <TestBOBO/>
                </TabPane>
            </Tabs>
        </div>
    );
}

export default App;
