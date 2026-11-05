import React, { useState } from "react";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { InputText } from "primereact/inputtext";
import "../shared/Table.css";

const Table = ({
  tableName,
  tableRef,
  id,
  sortField,
  rows,
  paginator,
  columns,
  data,
  tableClassName,
  children,
  style,
  footerColumnGroup,
  shouldHighlightRow,
  actionButtons,
}) => {
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <>
      <div className="flex align-items-center justify-content-between">
        <h2 className="m-0">{tableName}</h2>
        <div className="flex align-items-center">
          {actionButtons}

          <InputText
            value={globalFilter}
            onChange={(e) => setGlobalFilter(e.target.value)}
            placeholder="Search"
            className="m-3"
          />
        </div>
      </div>
      <DataTable
        ref={tableRef}
        id={id}
        value={data}
        className={tableClassName}
        paginator={paginator}
        rows={rows}
        globalFilter={globalFilter}
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink"
        removableSort
        sortField={sortField}
        style={style}
        size="small"
        footerColumnGroup={footerColumnGroup}
        rowClassName={(rowData) =>
          shouldHighlightRow && shouldHighlightRow(rowData)
            ? "highlighted-row"
            : ""
        }
      >
        {columns.map((col) => (
          <Column
            key={col.field}
            field={col.field}
            header={col.header}
            className={col.className}
            bodyClassName={`text-center ${col.bodyClassName || ""}`}
            headerClassName="text-center"
            body={col.body}
            sortable={col.sortable}
            style={col.style}
          />
        ))}
        {children}
      </DataTable>
    </>
  );
};

export default Table;
