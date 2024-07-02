import request from '@/utils/request.js';


export const borrowBooksAPI = ({ bookId, userId }) => {
  return request.post('/api/user/books/borrow', {
    bookId, userId
  })
}



export const returnBooksAPI = ({ bookId, userId }) => {
  return request.post('/api/user/books/return', {
    bookId, userId
  })
}

export const renewBooksAPI = (data) => {
  return request.post('/api/user/books/borrow/renew', data)
}



export const getUserBooksAPI = ({ pageNum, pageSize, title, isbn, userId }) => {
  return request({
    url: '/api/user/books',
    params: {
      pageNum,
      pageSize,
      title,
      isbn,
      userId
    },
  });
};

export const getUserBorrowInfoAPI = (userId) => {
  return request({
    url: '/api/user/books/info',
    params: {
      userId
    }
  })
}

export const getUserBorrowRecordAPI = (params) => {
  return request({
    url: '/api/borrow/user/record',
    params
  })
}


