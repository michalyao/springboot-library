// 封装所有和用户相关的接口函数
import request from '@/utils/request.js'

export const userInfoAPI = (token) => {
  return request({
    url: '/user',
    params: {
      token
    }
  })
}
