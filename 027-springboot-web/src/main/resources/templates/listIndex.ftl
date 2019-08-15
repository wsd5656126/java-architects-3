<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>freemaker其它用法</title>
</head>
<body>
${name}
<#if sex==2>
男
<#elseif sex==1>
女
</#if>
<#list listResult as str>
${str}
</#list>

</body>
</html>