# sys-campaignX

#### 1. API check SDT thuoc blacklist
- url: /api/check-blacklist
- method: POST
- type: json
- request:  
body: 
{
"msisdn" : "0911234567"
}
- response:  
body: 
{
    "code": 1,
    "message": ""
}
code = 1: SDT thuộc blacklist  
code = 0: SDT không thuộc blacklist  
code = -1: Lỗi hệ thống  
code khác: Xem message  

#### 1. API update SDT blacklist
- url: /api/update-blacklist
- method: POST
- type: json
- request:  
body: 
{
 "msisdn" : "354981c56135",
 "action": 0
}  
action = 1: Thêm mới  
action = 0: Xóa
- response:  
body: 
{
    "code": 0,
    "message": ""
}  
code = 1: Thành công  
code = -1: Lỗi hệ thống  
code khác: Xem message