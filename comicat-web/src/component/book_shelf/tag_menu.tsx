import React, {CSSProperties} from 'react';
import {Checkbox, CheckboxOptionType, Col, Divider, Radio} from "antd";
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

    optionMap: any = {}
    allCheck: string[] = []
    checkedList: string[] = []
    checkedAll = false
    indeterminateAll = false


    onChange = (e: any) => {
        this.checkedList = e
        this.checkedAll = this.checkedList.length === this.state.options.length
        this.indeterminateAll = this.checkedList.length > 0 && this.checkedList.length < this.state.options.length
        this.props.changeTags(e)
    }
    checkAll = (e: any) => {
        if (e.target.checked) {
            this.checkedList = this.allCheck
        } else {
            this.checkedList = []
        }
        this.checkedAll = e.target.checked
        this.indeterminateAll = false
        this.props.changeTags(this.checkedList)
    }

    handleSizeChange = (tagLogic: any) => this.props.changeTagLogic(tagLogic.target.value);

    componentDidMount() {
        api.getTagList({}).then(response => {
            if (response && response.data) {
                let options: CheckboxOptionType[] = [];
                response.data.tagList.map((o: any) => {
                    this.allCheck.push(o.id);
                    this.optionMap[o.id] = o.name
                    return options.push({label: o.name, value: o.id, disabled: false});
                })
                this.props.setTagMap(this.optionMap)
                this.setState({
                    options: options
                })

            }
        })
    }


    render() {
        return (
            <div>
                <Col>
                    <Radio.Group onChange={this.handleSizeChange} defaultValue={this.props.tagLogic}>
                        <Radio.Button value="and">与</Radio.Button>
                        <Radio.Button value="or">或</Radio.Button>
                    </Radio.Group>
                </Col>
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
