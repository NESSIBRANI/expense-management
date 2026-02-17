import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {

  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) {}

  // =========================
  // PENDING REPORTS
  // =========================
  getPendingReports(page: number = 0, size: number = 20) {
    return this.http.get<any>(`${this.apiUrl}/pending?page=${page}&size=${size}`);
  }

  // =========================
  // REPORT DETAILS (⭐ IMPORTANT)
  // =========================
  
getReportDetails(id:number){
  return this.http.get(`${environment.apiUrl}/reports/${id}`);
}

  // =========================
  // APPROVE REPORT
  // =========================
approveReport(id: number) {
  return this.http.put(`${this.apiUrl}/${id}/approve`, {});
}


  // =========================
  // REJECT REPORT
  // =========================
rejectReport(id: number, reason: string) {
  return this.http.put(`${this.apiUrl}/${id}/reject`, {
    reason: reason
  });
}





  // =========================
  // STATS
  // =========================
  getStats() {
    return this.http.get<any>(`${this.apiUrl}/stats`);
  }
}
