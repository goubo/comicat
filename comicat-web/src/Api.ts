import axios, {AxiosRequestConfig} from 'axios';
import qs from 'qs'
import {message} from 'antd';
import React from 'react';

const client = axios.create({
    baseURL: '/api',
    timeout: 5000,
    headers: {'X-Custom-Header': 'foobar'},

});


function request(config: AxiosRequestConfig) {
    return client.request(config).catch(e => {
        console.log(e)
        return message.error('接口调用失败').then(r => console.log(r));
    })
}

export class Api extends React.Component<any, any> {

    static getComicsList = (params: any) => request({
        method: 'GET',
        url: '/comics',
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        },
    })
    static getTagList = (params: any) => request({
        method: 'GET',
        url: '/tags',
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        }
    })

    static updateComics = (data: any) => request({
        url: '/comics',
        method: 'PATCH',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    })


    static addComics = (data: any) => request({
        url: '/comics',
        method: 'POST',
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    })


    static getConfig = () => request({
        method: 'GET',
        url: '/config',
    })
    static setConfig = (params: any) => request({
        url: '/config',
        method: 'POST',
        data: params,
    })


}








