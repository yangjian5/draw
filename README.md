## 首页


### 1、藏品
#### url 
get - /api/index
#### 入参

| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| type    |   int |  1：创造权；2：使用权  |
| sort    |   int |  1：价钱正序；2：价钱倒序  |
| max_id    |   int |  0：最新；分页传maxId ; -1 无数据 |

#### 出参

```
创造权 返回格式
{
	"code": 200,
	"message": "index",
	"data": {max_id:"2011-2",draws:[{
             		"id": 2,
             		"prod_uid": 12,
             		"prod_name": "",
             		"prod_tel": "4564",
             		"draw_name": "我的",
             		"draw_status": "1",
             		"auth_name": "yang",
             		"width" : 123,
             		"high" : 123,
             		"url_hd": "",
             		"url_simple": "",
             		"draw_desc": "1111111",
             		"own_count": 10,
             		"own_finish_count": 0,
             		"op_name": "",
             		"op_id": 0,
             		"create_time": "2019-07-22 14:24:13.0",
             		"modify_time": "2019-07-22 14:24:13.0"
             	}]}
	,
	"success": true
}

所有权 返回格式

{
    "code": 200,
    "message": "index",
    "data": {
        "max_id": "3-1",
        "draw_ext": [
            {
                "id": 3,
                "ext_uid": 3,
                "draw_id": 1,
                "ext_price": 2323,
                "op_name": "",
                "op_id": 0,
                "create_time": null,
                "modify_time": null,
                "draws": {
                    "id": 1,
                    "prod_uid": 12,
                    "prod_name": "",
                    "prod_tel": "123",
                    "draw_price": 2101,
                    "draw_name": "我的",
                    "draw_status": "1",
                    "draw_width": null,
                    "draw_high": null,
                    "auth_name": "yang",
                    "url_hd": "",
                    "url_simple": "",
                    "draw_desc": "1111111",
                    "own_count": 10,
                    "own_finish_count": 0,
                    "op_name": "",
                    "op_id": 0,
                    "create_time": "2019-07-22 14:24:13.0",
                    "modify_time": "2019-07-22 14:24:13.0"
                }
            },
            {
                "id": 1,
                "ext_uid": 2,
                "draw_id": 2,
                "ext_price": 1000,
                "op_name": "",
                "op_id": 0,
                "create_time": null,
                "modify_time": null,
                "draws": {
                    "id": 2,
                    "prod_uid": 12,
                    "prod_name": "",
                    "prod_tel": "4564",
                    "draw_price": 2101,
                    "draw_name": "我的",
                    "draw_status": "1",
                    "draw_width": null,
                    "draw_high": null,
                    "auth_name": "yang",
                    "url_hd": "",
                    "url_simple": "",
                    "draw_desc": "1111111",
                    "own_count": 10,
                    "own_finish_count": 0,
                    "op_name": "",
                    "op_id": 0,
                    "create_time": "2019-07-22 14:24:13.0",
                    "modify_time": "2019-07-22 14:24:13.0"
                }
            }
        ]
    },
    "success": true
}


```

---- 

###  2、登入、注册
#### url 
get - /api/login
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| code        |   str |  code  |
| avatar_url   |   str |  不为空可以给空串 avatarUrl= |
| nick_mame    |   str |  ...  |
| gender      |   str |  ...  |

#### 出参

```
{
	"code": 200,
	"message": "login",
	"data": {
		"income": "1205",
		"openid": "ov8p35OEtLxO7nILiHq6rmBCpkv4",
		"session_key": "LSH3Ho44x3G53ZZIEJewDQ==",
		"userid": 1
	},
	"success": true
}
```

---

### 3、查询用户信息
#### url 
get - /api/get_user
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |

#### 出参

```

{
	"code": 200,
	"message": "get_user",
	"data": {
		"id": 1,
		"open_id": "ov8p35OEtLxO7nILiHq6rmBCpkv4",
		"nick_name": "weqeq",
		"avatar_url": "123/132",
		"gender": "1",
		"income": "2134",
		"create_time": "2019-07-22 14:24:13.0",
		"modify_time": "2019-07-22 14:24:13.0"
	},
	"success": true
}

```

---

### 4、提现 - 企业付款 
#### url 
get - /api/withdrawal
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |
| money    |   int |  金额  |
| 安全内容    |   int |  安全内容  |


#### 出参

```
{
    "code": 200,
    "message": "withdrawal",
    "data": true,
    "success": true
}
```

---


### 6、上传藏品 - 基本信息
#### url 
post - /api/upload_image  全都是 form_data
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  open_id  |
| name    |   str |  姓名  |
| tel_no    |   str |  手机号 |
| draw_name    |   str |  作品名称  |
| author    |   str |  作者  |
| desc    |   str |  简介  |
| draw_file    |   file |  form_data   |

#### 出参

```
{
    "code": 200,
    "message": "upload_image",
    "data": true,
    "success": true
}
```

---

### 8、报表
#### url 
get - /api/statistics  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| range    |   str |  1、日7天 ；2、月；3、历史以来  |

#### 出参

```
{
    "code": 200,
    "message": "statistics",
    "data": {
        "create_prize_list": [
            {
                "id": 1,
                "draw_id": 4,
                "s_price": 1111,
                "create_time": "2019-07-31 20:31:33.0",
                "type": "1"
            }
        ],
        "owner_prize_list": [
            {
                "id": 2,
                "draw_id": 4,
                "s_price": 231,
                "create_time": "2019-07-31 20:31:59.0",
                "type": "2"
            }
        ],
        "create_income_list": [
            {
                "id": 1,
                "draw_id": 4,
                "income_price": 1,
                "create_time": "2019-07-31 20:29:26.0"
            },
            {
                "id": 2,
                "draw_id": 4,
                "income_price": 2,
                "create_time": "2019-07-04 20:26:06.0"
            },
            {
                "id": 3,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-05 20:26:06.0"
            },
            {
                "id": 4,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-06 20:26:06.0"
            },
            {
                "id": 5,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-07 20:26:06.0"
            },
            {
                "id": 6,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-08 20:26:06.0"
            },
            {
                "id": 7,
                "draw_id": 4,
                "income_price": 5,
                "create_time": "2019-07-31 20:29:02.0"
            },
            {
                "id": 8,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-10 20:26:06.0"
            },
            {
                "id": 9,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-11 20:26:06.0"
            },
            {
                "id": 10,
                "draw_id": 4,
                "income_price": 6,
                "create_time": "2019-07-31 20:29:06.0"
            },
            {
                "id": 11,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-13 20:26:06.0"
            },
            {
                "id": 12,
                "draw_id": 4,
                "income_price": 23,
                "create_time": "2019-07-31 20:29:08.0"
            },
            {
                "id": 13,
                "draw_id": 4,
                "income_price": 32,
                "create_time": "2019-07-31 20:29:10.0"
            },
            {
                "id": 14,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-16 20:26:06.0"
            },
            {
                "id": 15,
                "draw_id": 4,
                "income_price": 56,
                "create_time": "2019-07-31 20:29:12.0"
            },
            {
                "id": 16,
                "draw_id": 4,
                "income_price": 9,
                "create_time": "2019-07-31 20:29:14.0"
            },
            {
                "id": 17,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-19 20:26:06.0"
            },
            {
                "id": 18,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-20 20:26:06.0"
            },
            {
                "id": 19,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-21 20:26:06.0"
            },
            {
                "id": 20,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-22 20:26:06.0"
            },
            {
                "id": 21,
                "draw_id": 4,
                "income_price": 56,
                "create_time": "2019-07-31 20:29:16.0"
            },
            {
                "id": 22,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-24 20:26:06.0"
            },
            {
                "id": 23,
                "draw_id": 4,
                "income_price": 8,
                "create_time": "2019-07-31 20:29:18.0"
            },
            {
                "id": 24,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-26 20:26:06.0"
            },
            {
                "id": 25,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-27 20:26:06.0"
            },
            {
                "id": 26,
                "draw_id": 4,
                "income_price": 9,
                "create_time": "2019-07-31 20:29:19.0"
            },
            {
                "id": 27,
                "draw_id": 4,
                "income_price": 6,
                "create_time": "2019-07-31 20:29:21.0"
            },
            {
                "id": 28,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-30 20:26:06.0"
            },
            {
                "id": 29,
                "draw_id": 4,
                "income_price": 4,
                "create_time": "2019-07-31 20:26:06.0"
            }
        ]
    },
    "success": true
}
```
---

### 9、推送到花屏
#### url 
get - /api/push  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

---

### 10、购买
#### url 
get - /api/buy  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| type    |   str |  1、创作权；2、所有权 |
| open_id    |   str |  openid |
| tel    |   str |  购买人电话 |
| name   |   str |  购买人姓名 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```
---

### 10、我的订单
#### url 
get - /api/my_order  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openid |
| status    |   str |  1-提交未支付 2-支付完成 3-过期 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```
---

### 12、成功支付回调。（后端使用）
#### url 
get - /api/wx_notify  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| type    |   str |  1、创作权；2、所有权 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

---

### 13、我的藏品
#### url 
get - /api/my_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |
| max_id    |   int |  0：最新；分页传maxId  |
| type    |   int |  1、创造权；2、所有权  |


#### 出参

```
创造权
{
    "code": 200,
    "message": "index",
    "data": {
        "max_id": "1",
        "draws": [
            {
                "id": 1,
                "prod_uid": 1,
                "prod_name": "",
                "prod_tel": "123",
                "draw_price": 2101,
                "draw_name": "我的",
                "draw_status": "1",
                "draw_width": null,
                "draw_high": null,
                "auth_name": "yang",
                "url_hd": "",
                "url_simple": "",
                "draw_desc": "1111111",
                "own_count": 10,
                "own_finish_count": 0,
                "op_name": "",
                "op_id": 0,
                "create_time": "2019-07-22 14:24:13.0",
                "modify_time": "2019-07-22 14:24:13.0"
            },
            {
                "id": 2,
                "prod_uid": 1,
                "prod_name": "",
                "prod_tel": "4564",
                "draw_price": 2101,
                "draw_name": "我的",
                "draw_status": "1",
                "draw_width": null,
                "draw_high": null,
                "auth_name": "yang",
                "url_hd": "",
                "url_simple": "",
                "draw_desc": "1111111",
                "own_count": 10,
                "own_finish_count": 0,
                "op_name": "",
                "op_id": 0,
                "create_time": "2019-07-22 14:24:13.0",
                "modify_time": "2019-07-22 14:24:13.0"
            }
        ]
    },
    "success": true
}

所有权

{
    "code": 200,
    "message": "index",
    "data": {
        "max_id": "1-1",
        "draw_ext": [
            {
                "id": 1,
                "ext_uid": 1,
                "draw_id": 2,
                "ext_price": 1000,
                "op_name": "",
                "op_id": 0,
                "create_time": null,
                "modify_time": null,
                "draws": {
                    "id": 2,
                    "prod_uid": 1,
                    "prod_name": "",
                    "prod_tel": "4564",
                    "draw_price": 2101,
                    "draw_name": "我的",
                    "draw_status": "1",
                    "draw_width": null,
                    "draw_high": null,
                    "auth_name": "yang",
                    "url_hd": "",
                    "url_simple": "",
                    "draw_desc": "1111111",
                    "own_count": 10,
                    "own_finish_count": 0,
                    "op_name": "",
                    "op_id": 0,
                    "create_time": "2019-07-22 14:24:13.0",
                    "modify_time": "2019-07-22 14:24:13.0"
                }
            }
        ]
    },
    "success": true
}
```

### 14、修改藏品
#### url 
get - /api/update_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |   |
| create_price    |   int |  创造这价格 分 |
| owner_count    |   int |  所有权份额 分 |
| is_sale    |   str |  0-不出售 1-出售 分 |

#### 出参

```
{
	"code": 200,
	"message": "upload_image",
	"data": true,
	"success": true
}
```

### 15、修改藏品所有权价格
#### url 
get - /api/update_owner_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_ext_id    |   str |   |
| owner_prize  |   int |  所有权份额 分 |
| income_prize  |   int |  所有权份额 分 |
| proof  |   file | 凭证 |


#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

### 16、操作流水
#### url 
get - /api/op_log  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |   |


#### 出参

```
{
	time:10000
	code:200
	data:true
}
```
类型 1-创造权交易 2-所有权交易收益 3-所有权添加收益 4-提现


### 17、用户管理
#### url 
get - /api/user/select   
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| nickName     |   str |  昵称 |
| page    |   int |1  |
| count    |   int | 10  |


#### 出参

```

{
    "code": 200,
    "message": "userSelect",
    "data": {
        "page": 1,
        "count": 10,
        "total_count": 2,
        "users": [
            {
                "id": 1,
                "open_id": "ov8p35OEtLxO7nILiHq6rmBCpkv4",
                "nick_name": "楊∮",
                "avatar_url": "https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132",
                "gender": "1",
                "income": 100000,
                "create_time": "2019-07-22 14:24:13.0",
                "modify_time": "2019-07-22 14:24:13.0"
            },
            {
                "id": 2,
                "open_id": "oe8p35OEtLxO7nILiHq6rmBCpkv4",
                "nick_name": "楊∮",
                "avatar_url": "https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83epnRkt8ueSLqcIUPr7XKaNwmn3y0OvoyFjaNRXwByvtKeZ1JlCdCbuqrELauJflWuPkvVcF7s48Bw/132",
                "gender": "1",
                "income": 0,
                "create_time": "2019-07-22 14:24:13.0",
                "modify_time": "2019-07-22 14:24:13.0"
            }
        ]
    },
    "success": true
}

```


### 18、图片管理
#### url 
get - /api/draw/select  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| drawName    |   str |画名称  |
| page    |   int |1  |
| count    |   int | 10  |


### 18、图片审核 
#### url 
get - /api/draw/check
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| id     |   int |  图片ID |
| drawStatus    |   int |画作状态 0-未审核 1-审核通过 2-审核不通过 |



### 19、banner update
#### url 
get - /api/banner/update
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| id     |   int |  banner id |
| brannerUrl     |   str |  bannerUrl 图片地址 |
| clickUrl    |   str | 点击url  |
| type    |   int |类型 1-外部图 2-创造者 |
| drawId    |   int | 图片ID  |
| sort    |   int | 排序  |



### 19、banner insert
#### url 
get - /api/banner/update
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| brannerUrl     |   str |  bannerUrl 图片地址 |
| clickUrl    |   str | 点击url  |
| type    |   int |类型 1-外部图 2-创造者 |
| drawId    |   int | 图片ID  |
| sort    |   int | 排序  |



### 19、banner delete
#### url 
get - /api/banner/update
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| id     |   int |  banner id |




#### 出参

```
{
    "code": 200,
    "message": "drawSelect",
    "data": {
        "max_id": null,
        "draws": [
            {
                "id": 17,
                "prod_uid": 1,
                "prod_name": "yangjian",
                "prod_tel": "13200008888",
                "draw_price": 0,
                "draw_name": "哪吒脑海",
                "draw_status": "1",
                "draw_width": 430,
                "draw_high": 760,
                "auth_name": "还算",
                "url_hd": "https://www.aiwsport.com/data1/draw_simple/1564885525478_WX20190618-103633@2x.png",
                "url_simple": "https://aiwsport.com/data1/draw/1564902854526_WX20190618-103633@2x.png",
                "draw_desc": "萨达奥大所",
                "own_count": 0,
                "own_finish_count": 0,
                "op_name": null,
                "op_id": null,
                "create_time": "2019-08-04 10:25:43.0",
                "modify_time": "2019-08-04 10:25:43.0"
            }
        ],
        "page": 1,
        "count": 10,
        "total_count": 4
    },
    "success": true
}
```




类型 1-创造权交易 2-所有权交易收益 3-所有权添加收益 4-提现






### 
所有url 后加 .json
- [X]  藏品(首页)
- [X]  登入、注册
- [X]  查询用户信息
- [X]  上传藏品 - 基本信息
- [X]  修改藏品
- [X]  我的藏品
- [X]  修改藏品所有权价格 或上传收益
- [X]  报表
- [X]  我的订单


完成 90%
- [X] 购买
- [X] 支付成功回调
- [X] 提现






