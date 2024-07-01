// 封装所有和用户相关的接口函数
import request from '@/utils/request.js'

export const userInfoAPI = (token) => {
  return request({
    url: '/api/user',
    params: {
      token
    }
  })
}

export const updateUserAPI = (form) => {
  return request.put(`/api/user/${form.id}`, form)
}

export const updateUserPasswordAPI = ({ userId, oldPassword, newPassword }) => {
  return request.post(`/api/user/password`, {
    userId,
    oldPassword,
    newPassword
  })
}