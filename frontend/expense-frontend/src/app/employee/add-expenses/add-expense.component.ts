import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MaterialModule } from '../../material.module';
import { ExpenseService } from '../../services/expense.service';



@Component({
  selector: 'app-add-expense',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule],
  templateUrl: './add-expense.html',
  styleUrls: ['./add-expense.css']
})
export class AddExpenseComponent {
  reportId!: number;
  loading = false;
  successMessage = '';
  errorMessage = '';

  expense = {
    title: '',
    amount: 0,
    date: new Date().toISOString().substring(0, 10)
  };

  constructor(
    private expenseService: ExpenseService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.reportId = Number(this.route.snapshot.paramMap.get('reportId'));
  }

  submitExpense(form: NgForm) {
    if (!this.reportId) {
      this.errorMessage = '❌ reportId manquant dans l’URL';
      return;
    }

    this.loading = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.expenseService.createExpense(this.reportId, this.expense).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = '✅ Dépense ajoutée';

        form.resetForm({
          title: '',
          amount: 0,
          date: new Date().toISOString().substring(0, 10)
        });
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.errorMessage = '❌ Erreur lors de l’ajout';
      }
    });
  }

  goBack() {
    this.router.navigate(['/employee/reports']);
  }
}
