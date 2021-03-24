import React, {CSSProperties} from 'react';
import {Checkbox, CheckboxOptionType, Divider} from "antd";
import {api} from "../../api";

const CheckboxGroup = Checkbox.Group;


const checkboxGroupStyle: CSSProperties = {
    width: '100%', display: 'flex', flexDirection: 'column'
}


export class TagMenu extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            options: []
        }
    }

    allCheck: string[] = []
    checkedList: string[] = []
    checkedAll = false
    indeterminateAll = false


    onChange = (e: any) => {
        this.checkedList = e
        this.checkedAll = this.checkedList.length === this.state.options.length
        this.indeterminateAll = this.checkedList.length > 0 && this.checkedList.length < this.state.options.length
        this.props.changeMenu(e)
    }
    checkAll = (e: any) => {
        if (e.target.checked) {
            this.checkedList = this.allCheck
        } else {
            this.checkedList = []
        }
        this.checkedAll = e.target.checked
        this.indeterminateAll = false
        this.props.changeMenu(this.checkedList)
    }

    componentDidMount() {
        api.getTagList({}).then(response => {
            if (response && response.data) {
                let options: CheckboxOptionType[] = [];
                response.data.tagList.map((o: any) => {
                    this.allCheck.push(o.id);
                    return options.push({label: o.name, value: o.id, disabled: false});
                })

                this.setState({
                    options: options
                })

            }
        })
    }


    render() {
        return (
            <div>
                <Checkbox onChange={this.checkAll} checked={this.checkedAll} indeterminate={this.indeterminateAll}>
                    Check all
                </Checkbox>
                <Divider/>
                <CheckboxGroup style={checkboxGroupStyle} name={"tags_check_box"} options={this.state.options}
                               value={this.checkedList} onChange={this.onChange}/>

            </div>
        );
    }
}
