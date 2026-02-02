import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-admin-employees',
  standalone: true,
  imports: [CommonModule, MatTableModule],
  templateUrl: './admin-employees.html',
  styleUrls: ['./admin-employees.css']
})
export class AdminEmployeesComponent {

  displayedColumns = ['id', 'name', 'email', 'role'];

  dataSource = [
    { id: 1, name: 'Ahmed Mohamed', email: 'ahmed@mail.com', role: 'EMPLOYEE' },
    { id: 2, name: 'Fatima Ali', email: 'fatima@mail.com', role: 'EMPLOYEE' }
  ];
}
