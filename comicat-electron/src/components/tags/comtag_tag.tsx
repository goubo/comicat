import React from 'react'
import {Tag} from 'antd';

export const ComTagTag = (props: { value: string | object | any }) => {
    if (!props.value) return <></>
    if (typeof props.value === 'object')
        return props.value.map((s: string) => {
            return <Tag key={s} style={{marginBottom: '1vh'}}>{s}</Tag>
        })
    else
        return props.value.split(',').filter((s: string) => s).map((s: string) => {
            return <Tag key={s} style={{marginBottom: '1vh'}}>{s}</Tag>
        })
}
