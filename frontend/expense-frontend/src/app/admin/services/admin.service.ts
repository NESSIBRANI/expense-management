import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, map, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminService {

  private reportsUrl = 'http://localhost:8081/api/admin/reports';

  constructor(private http: HttpClient) {}

  /* =========================
     GET ALL REPORTS (MERGED)
     ========================= */
  getAllReports(): Observable<any[]> {
    return forkJoin({
      approved: this.http.get<any>(`${this.reportsUrl}/approved?page=0&size=100`),
      paid: this.http.get<any>(`${this.reportsUrl}/paid?page=0&size=100`)
    }).pipe(
      map(res => {
        const approved = res.approved.content || [];
        const paid = res.paid.content || [];

        return [...approved, ...paid];
      })
    );
  }

  /* =========================
     STATS CALCULATED FROM ALL
     ========================= */
  getStats(): Observable<any> {
    return this.getAllReports().pipe(
      map((reports: any[]) => {

        let total = 0;
        let paid = 0;
        let approved = 0;
        let rejected = 0;

        reports.forEach((r: any) => {

          const reportTotal = (r.items || []).reduce(
            (sum: number, item: any) => sum + (item.amount || 0),
            0
          );

          total += reportTotal;

          if (r.status === 'PAID') paid += reportTotal;
          if (r.status === 'APPROVED') approved += reportTotal;
          if (r.status === 'REJECTED') rejected += reportTotal;
        });

        return { total, paid, approved, rejected };
      })
    );
  }

  /* =========================
     REPORTS FOR DASHBOARD
     ========================= */
  getReports(): Observable<any[]> {
    return this.getAllReports().pipe(
      map(reports =>
        reports
          .sort((a, b) =>
            new Date(b.createdAt).getTime() -
            new Date(a.createdAt).getTime()
          )
          .slice(0, 5)
      )
    );
  }
}