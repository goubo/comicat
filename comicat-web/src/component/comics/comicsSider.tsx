import React, {CSSProperties} from 'react';
import {Checkbox, Col, Divider, Radio} from 'antd';

const CheckboxGroup = Checkbox.Group;


const checkboxGroupStyle: CSSProperties = {
    width: '100%', display: 'flex', flexDirection: 'column'
}

/**
 *  左侧边框,标签列表
 */
export class ComicsSider extends React.Component<any, any> {

    checkedList: string[] = []
    checkedAll = false
    tagLogic = this.props.tagLogic
    indeterminateAll = false


    onChange = (e: any) => {
        this.checkedList = e
        this.checkedAll = this.checkedList.length === this.props.tagsList.length
        this.indeterminateAll = this.checkedList.length > 0 && this.checkedList.length < this.props.tagsList.length
        this.returnQueryParams()
    }
    checkAll = (e: any) => {
        if (e.target.checked) {
            this.checkedList = this.props.tagsList
        } else {
            this.checkedList = []
        }
        this.checkedAll = e.target.checked
        this.indeterminateAll = false
        this.returnQueryParams()
    }

    handleSizeChange = (tagLogic: any) => {
        this.tagLogic = tagLogic.target.value
        this.returnQueryParams()
    };

    returnQueryParams = () => {
        this.props.changeQueryParams({tagLogic: this.tagLogic, comicsTags: this.checkedList})
    }


    render() {
        return (
            <div>
                <Col>
                    <Radio.Group onChange={this.handleSizeChange} defaultValue={this.props.tagLogic}>
                        <Radio.Button value='and'>与</Radio.Button>
                        <Radio.Button value='or'>或</Radio.Button>
                    </Radio.Group>
                </Col>
                <Checkbox onChange={this.checkAll} checked={this.checkedAll} indeterminate={this.indeterminateAll}>
                    Check all
                </Checkbox>
                <Divider/>
                <CheckboxGroup style={checkboxGroupStyle} name={'tags_check_box'} options={this.props.tagsList}
                               value={this.checkedList} onChange={this.onChange}/>
            </div>
        );
    }

}
