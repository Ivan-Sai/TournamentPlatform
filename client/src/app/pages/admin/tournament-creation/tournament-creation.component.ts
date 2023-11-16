import {Component, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TournamentService} from "../../../services/TournamentService";
import {Router} from "@angular/router";

@Component({
  selector: 'app-tournament-creation',
  standalone: true,
  templateUrl: './tournament-creation.component.html',
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  styleUrls: ['./tournament-creation.component.css']
})
export class TournamentCreationComponent implements OnInit {

  public maxMin: boolean = true;
  public minCorrect: boolean = true;
  public maxCorrect: boolean = true;
  public dateBefore: boolean = true;
  public dateAfter: boolean = true;
  public minDate: string | undefined;
  public maxDate: string | undefined;
  public errorMessage: string = '';
  constructor(private fb: FormBuilder,
              private tournamentService: TournamentService,
              private router: Router,
  ) {
  }

  ngOnInit(): void {
    this.form.valueChanges.subscribe(value => {
      this.minDate = new Date(new Date().getTime() + 16 * 60000).toLocaleString().slice(0, -3);
      this.maxDate = new Date(new Date().getFullYear(), new Date().getMonth() + 2, new Date().getDate()).toLocaleDateString();
      this.maxMin = this.maxMinCheck(value['maxTeams'], value['minTeams']);
      this.minCorrect = this.checkMinFormat(value['minTeams']);
      this.maxCorrect = this.checkMaxFormat(value['maxTeams']);
      this.dateCheck(value['startDate'])
    })
  }

  public form: FormGroup = this.fb.group({
    name: new FormControl(null, [Validators.required]),
    // image: new FormControl(null, [Validators.required,]),
    discipline: new FormControl('Dota 2', Validators.required),
    maxTeams: new FormControl(null, [Validators.required, Validators.min(2), Validators.max(128)]),
    minTeams: new FormControl(null, [Validators.required, Validators.min(2), Validators.max(128)]),
    startDate: new FormControl(null, [Validators.required]),
    tournamentType: new FormControl('TYPE_5VS5', Validators.required),
  });

  public onSubmit() {
      this.tournamentService.create(this.form.value).subscribe(value => {
        // @ts-ignore
        this.router.navigateByUrl('tournaments/' + value['tournamentId'])

      },
        error => {
        this.errorMessage = error.error;
        })
  }

  private checkMinFormat(min: number) {
    if (min !== null) {
      return !(min < 2 || min > 128)
    }
    return true
  }

  private checkMaxFormat(max: number) {
    if (max !== null) {
      return !(max < 2 || max > 128)
    }
    return true
  }

  private maxMinCheck(max: number, min: number): boolean {
    if (max != null && min != null) return max >= min;
    return true
  }

  private dateCheck(dateStr: Date): void {
    if (dateStr !== null) {
      const date = new Date(dateStr)
      const currentDate = new Date();
      // const minDate = new Date(currentDate.getTime() + 15 * 60000); // текущая дата + 15 минут
      const minDate = new Date();
      const maxDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 2, currentDate.getDate()); // текущая дата + 2 месяца

      this.dateBefore = date >= minDate;
      this.dateAfter = date <= maxDate;
    }
  }

}
