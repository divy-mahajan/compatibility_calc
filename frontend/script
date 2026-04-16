const API = 'http://localhost:8080';

// ── State ──────────────────────────────────
let state = {
  name1:    '',
  name2:    '',
  questions: [],
  answers1: {},
  answers2: {},
  currentPerson:   0,   // 0 = person 1, 1 = person 2
  currentQuestion: 0,
  selectedOption:  null,
};

// ── Page helpers ───────────────────────────
function showPage(id) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  window.scrollTo(0, 0);
}

function showError(elId, msg) {
  const el = document.getElementById(elId);
  el.textContent = msg;
  el.style.display = 'block';
  setTimeout(() => el.style.display = 'none', 4000);
}

// ── PAGE 1: Start ──────────────────────────
async function startTest() {
  const n1 = document.getElementById('name1').value.trim();
  const n2 = document.getElementById('name2').value.trim();

  if (!n1 || !n2) {
    showError('name-error', 'Please enter both names before continuing.');
    return;
  }
  if (n1.length > 50 || n2.length > 50) {
    showError('name-error', 'Names must be 50 characters or fewer.');
    return;
  }

  state.name1 = n1;
  state.name2 = n2;
  state.answers1 = {};
  state.answers2 = {};
  state.currentPerson = 0;
  state.currentQuestion = 0;

  // Fetch questions from backend
  try {
    const res = await fetch(`${API}/questions`);
    if (!res.ok) throw new Error('Could not load questions.');
    state.questions = await res.json();
  } catch (e) {
    showError('name-error', '⚠ Could not connect to the server. Make sure the backend is running on port 8080.');
    return;
  }

  showPage('page-questions');
  renderQuestion();
}

// ── PAGE 2: Questions ──────────────────────
function renderQuestion() {
  const q    = state.questions[state.currentQuestion];
  const name = state.currentPerson === 0 ? state.name1 : state.name2;
  const total = state.questions.length;
  const num   = state.currentQuestion + 1;

  document.getElementById('progress-person').textContent = `${name}'s turn`;
  document.getElementById('progress-count').textContent  = `${num} / ${total}`;

  // Progress bar: person 1 uses 0–50%, person 2 uses 50–100%
  const base = state.currentPerson === 0 ? 0 : 50;
  const pct  = base + ((num / total) * 50);
  document.getElementById('progress-fill').style.width = pct + '%';

  document.getElementById('question-text').textContent = q.text;

  // Render options
  const container = document.getElementById('options-container');
  container.innerHTML = '';
  q.options.forEach((opt, i) => {
    const btn = document.createElement('button');
    btn.className = 'option';
    btn.textContent = opt;
    btn.onclick = () => selectOption(i);
    container.appendChild(btn);
  });

  state.selectedOption = null;
  const nextBtn = document.getElementById('next-btn');
  nextBtn.disabled = true;
  nextBtn.textContent = isLastQuestion() ? 'See Results →' : 'Next →';
}

function selectOption(index) {
  state.selectedOption = index;
  document.querySelectorAll('.option').forEach((btn, i) => {
    btn.classList.toggle('selected', i === index);
  });
  document.getElementById('next-btn').disabled = false;
}

function isLastQuestion() {
  return state.currentQuestion === state.questions.length - 1;
}

function handleNext() {
  if (state.selectedOption === null) return;

  const q     = state.questions[state.currentQuestion];
  const value = q.values[state.selectedOption];

  if (state.currentPerson === 0) {
    state.answers1[q.id] = value;
  } else {
    state.answers2[q.id] = value;
  }

  if (isLastQuestion()) {
    if (state.currentPerson === 0) {
      // Switch to person 2
      state.currentPerson   = 1;
      state.currentQuestion = 0;
      renderQuestion();
    } else {
      // Both done — calculate
      submitAnswers();
    }
  } else {
    state.currentQuestion++;
    renderQuestion();
  }
}

// ── PAGE 3: Result ─────────────────────────
async function submitAnswers() {
  showPage('page-result');
  document.getElementById('loading').style.display = 'block';
  document.getElementById('result-content').style.display = 'none';

  const payload = {
    name1:    state.name1,
    name2:    state.name2,
    answers1: state.answers1,
    answers2: state.answers2,
  };

  try {
    const res = await fetch(`${API}/calculate`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(payload),
    });
    if (!res.ok) throw new Error('Server error: ' + res.status);
    const data = await res.json();
    renderResult(data);
  } catch (e) {
    document.getElementById('loading').style.display = 'none';
    document.getElementById('result-content').style.display = 'block';
    document.getElementById('compat-label').textContent  = 'Connection Error';
    document.getElementById('compat-insight').textContent = '⚠ Could not reach the backend. Make sure Spring Boot is running on port 8080.';
  }
}

function renderResult(data) {
  document.getElementById('loading').style.display = 'none';
  document.getElementById('result-content').style.display = 'block';

  // Animate score ring (circumference = 2π × 70 ≈ 439.8)
  const circumference = 439.8;
  const offset = circumference - (data.score / 100) * circumference;
  setTimeout(() => {
    document.getElementById('ring-fg').style.strokeDashoffset = offset;
  }, 100);

  // Animate score number
  animateNumber('score-num', 0, data.score, 1200);

  document.getElementById('compat-label').textContent   = data.label;
  document.getElementById('compat-insight').textContent = data.insight;

  // Personality cards
  const row = document.getElementById('personality-row');
  row.innerHTML = `
    <div class="personality-card">
      <div class="person-name">${esc(state.name1)}</div>
      <div class="mbti-code">${esc(data.code1)}</div>
      <div class="mbti-archetype">${esc(data.archetype1)}</div>
    </div>
    <div class="personality-card">
      <div class="person-name">${esc(state.name2)}</div>
      <div class="mbti-code">${esc(data.code2)}</div>
      <div class="mbti-archetype">${esc(data.archetype2)}</div>
    </div>
  `;

  // Trait breakdown
  const TRAIT_LABELS = {
    social:     'Social Energy',
    perceiving: 'Perceiving Style',
    empathy:    'Empathy',
    creativity: 'Creativity',
    ambition:   'Ambition',
    adventure:  'Adventurousness',
    conflict:   'Conflict Style',
    learning:   'Learning Style',
    decision:   'Decision-Making',
    schedule:   'Schedule Pref.',
  };

  const breakdown = document.getElementById('trait-breakdown');
  breakdown.innerHTML = '';

  Object.entries(data.dimensionScores || {}).forEach(([key, pct]) => {
    const label = TRAIT_LABELS[key] || key;
    const row = document.createElement('div');
    row.className = 'trait-row';
    row.innerHTML = `
      <span class="trait-name">${esc(label)}</span>
      <div class="trait-bar"><div class="trait-fill" data-pct="${pct}"></div></div>
      <span class="trait-pct">${pct}%</span>
    `;
    breakdown.appendChild(row);
  });

  // Animate trait bars
  setTimeout(() => {
    document.querySelectorAll('.trait-fill').forEach(bar => {
      bar.style.width = bar.dataset.pct + '%';
    });
  }, 300);
}

// ── Helpers ────────────────────────────────
function animateNumber(id, from, to, duration) {
  const el    = document.getElementById(id);
  const start = performance.now();
  function frame(now) {
    const progress = Math.min((now - start) / duration, 1);
    const ease     = 1 - Math.pow(1 - progress, 3);
    el.textContent = Math.round(from + (to - from) * ease);
    if (progress < 1) requestAnimationFrame(frame);
  }
  requestAnimationFrame(frame);
}

function esc(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function restart() {
  state.currentPerson = 0;
  state.currentQuestion = 0;
  state.answers1 = {};
  state.answers2 = {};
  state.selectedOption = null;
  document.getElementById('name1').value = '';
  document.getElementById('name2').value = '';
  showPage('page-names');
}

// Allow Enter key on name inputs
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('name2').addEventListener('keydown', e => {
    if (e.key === 'Enter') startTest();
  });
  document.getElementById('name1').addEventListener('keydown', e => {
    if (e.key === 'Enter') document.getElementById('name2').focus();
  });
});
