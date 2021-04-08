import React from 'react'
import {Tag} from 'antd';

export class ComTagTag extends React.Component<any, any> {
    render() {
        if (!this.props.value) return <></>
        if (typeof this.props.value === 'object' )
            return this.props.value.map((s: string) => {
                return <Tag key={s} style={{marginBottom: '1vh'}}>{s}</Tag>
            })
        else
            return this.props.value.split(',').filter((s: string) => s).map((s: string) => {
                return <Tag key={s} style={{marginBottom: '1vh'}}>{s}</Tag>
            })
    }


}
