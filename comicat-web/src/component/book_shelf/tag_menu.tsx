import React, {CSSProperties} from 'react';
import {Checkbox, Divider} from "antd";

const CheckboxGroup = Checkbox.Group;
const plainOptions = ['Apple', 'Pear', 'Orange'];

const checkboxGroupStyle: CSSProperties = {
    width: '100%', display: 'flex', flexDirection: 'column'
}


export class TagMenu extends React.Component<any, any> {
    checkedList: any = []
    checkedAll = false
    indeterminateAll = false

    onChange = (e: any) => {
        this.checkedList = e
        this.checkedAll = this.checkedList.length == plainOptions.length
        this.indeterminateAll = this.checkedList.length > 0 && this.checkedList.length < plainOptions.length
        this.props.changeMenu(e)
    }
    checkAll = (e: any) => {
        if (e.target.checked) {
            this.checkedList = plainOptions
        } else {
            this.checkedList = []
        }
        this.checkedAll = e.target.checked
        this.indeterminateAll = false
        this.props.changeMenu(this.checkedList)
    }


    render() {
        return (
            <div>
                <Checkbox onChange={this.checkAll} checked={this.checkedAll} indeterminate={this.indeterminateAll}>
                    Check all
                </Checkbox>
                <Divider/>
                <CheckboxGroup style={checkboxGroupStyle} name={"tags_check_box"} options={plainOptions}
                               value={this.checkedList} onChange={this.onChange}/>

            </div>
        );
    }
}
