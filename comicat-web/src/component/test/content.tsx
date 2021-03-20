import React from 'react';

const countStyle = {
    margin: "10px",
    padding: "10px"
}

export default class Content extends React.Component<any, any> {

    constructor(props: any) {
        // 属性一般都是在父界面中引用子组件的时候赋值，传递过来的。
        super(props);
        // 初始化数量为
        this.state = {
            count: this.props.value
        };
        //  要在函数内部使用 this.state/this.props必须绑定this
        this.onClickButton = this.onClickButton.bind(this);
        this.onClickButtonTwo = this.onClickButtonTwo.bind(this);

        this.updateInfo = this.updateInfo.bind(this);
    }

    // 定义单机方法
    onClickButton() {
        this.updateInfo(true);
    }

    onClickButtonTwo() {
        this.updateInfo(false);
    }

    updateInfo(state: boolean) {
        const firstValue = this.state.count;
        let newValue = 0;
        if (state) {
            newValue = firstValue + 1;
        } else {
            if (firstValue > 0) {
                newValue = firstValue - 1;
            }
        }

        // 更新子组件状态
        this.setState(
            {
                count: newValue
            }
        )
        // 更新父组件 props, 传递新的数量和原来的数量
        this.props.onUpDate(newValue, firstValue);
    }

    render() {
        return (
            <div style={countStyle}>
                <button onClick={this.onClickButton}>+</button>
                <button onClick={this.onClickButtonTwo}>—</button>
                <div>
                    {this.state.count}
                </div>
            </div>
        );


    }

}
