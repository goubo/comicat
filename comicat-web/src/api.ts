import axios from "axios";
import qs from 'qs'

const client = axios.create({
    baseURL: '/api',
    timeout: 2000,
    headers: {'X-Custom-Header': 'foobar'}
});


export class api extends Function {
    static getComicsList = (params: any) => client.get('/comics', {
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        }
    }).catch(e => {
        console.log(e)
    })

    static getTagList = (params: any) => client.get('/tags', {
        params: params,
        paramsSerializer: params => {
            return qs.stringify(params, {indices: false})
        }
    }).catch(e => {
        console.log(e)
    })

}








