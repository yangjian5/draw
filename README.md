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
| avatarUrl   |   str |  不为空可以给空串 avatarUrl= |
| nickName    |   str |  ...  |
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
	time:10000
	code:200
	data:{
		create_income_list:[],
		create_prize_list:[],
		owner_income_list[],
		owner_prize_list[],

	}
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
        "max_id": "1",
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

### 
所有url 后加 .json
- [X]  藏品(首页)
- [X]  登入、注册
- [X]  查询用户信息
- [X]  上传藏品 - 基本信息
- [X]  修改藏品
- [X]  我的藏品
- [X]  修改藏品所有权价格


完成 70%
- [X] 下单
- [X] 支付成功回调






