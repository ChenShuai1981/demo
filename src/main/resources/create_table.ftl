CREATE TABLE ${table.name} (
    <#list 0..(table.columns!?size-1) as i>
    <#--<#list table.columns! as column>-->
        ${table.columns[i].name!} ${table.columns[i].type!}<#if i != table.columns!?size-1>,</#if>
    </#list>
) WITH (
    <#--<#list table.dataSource.properties! as property>-->
    <#list 0..(table.dataSource.properties!?size-1) as i>
    '${table.dataSource.properties[i].key!}' = '${table.dataSource.properties[i].value!}'<#if i != table.dataSource.properties!?size-1>,</#if>
    </#list>
);