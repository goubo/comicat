import axios from "axios";
import qs from 'qs'
import {message} from "antd";
import React from "react";

const client = axios.create({
    baseURL: '/api',
    timeout: 2000,
    headers: {'X-Custom-Header': 'foobar'}
});


export class Api extends React.Component<any, any> {

    static getComicsList = (params: any) => client.request({
        url: "/comics",
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        },
    }).catch(e => {
        message.error(e).then(() => {
        });
    })


    static getTagList = (params: any) => client.get('/tags', {
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        }
    }).catch(e => {
        message.error(e).then(() => {
        });
    })

    static addComics = (data: any) => client.request({
        url: "/comics",
        method: "POST",
        data: data,
    }).catch(e => {
        message.error("接口调用失败").then(() => {
        });
    })


    static getConfig = () => client.get('/config').catch(e => {
        console.log(e)
    })


}








