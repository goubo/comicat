import React from 'react';
import Content from "./content";

export default class TestBOBO extends React.Component<any, any> {
    private readonly initArr: number[];

    constructor(props: any) {
        super(props);
        this.totalCountFun = this.totalCountFun.bind(this);
        this.initArr = [4, 5];
        this.state = {
            total: this.initArr[0] + this.initArr[1]
        }
    }

    totalCountFun(newVal: number, first: number) {
        console.log(first, newVal);
        let totalNum = this.state.total;
        if (newVal > first) {
            totalNum = totalNum + 1;
        }
        if (newVal < first) {
            totalNum = totalNum - 1;
        }
        this.setState(
            {total: totalNum}
        )
    }

    render() {
        return (
            <div className="App">

                <Content value={this.initArr[0]} onUpDate={this.totalCountFun}/>
                <Content value={this.initArr[1]} onUpDate={this.totalCountFun}/>

                <div>
                    总数：{this.state.total}
                </div>
            </div>
        );
    }
}
