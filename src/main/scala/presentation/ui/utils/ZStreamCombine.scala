package presentation.ui.utils

import zio.stream.ZStream

def combine[R1, R2, E1, E2, A1, A2](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2]
): ZStream[R1 & R2, E1 | E2, (A1, A2)] =
  p1 zipLatest p2

def combine[R1, R2, R3, E1, E2, E3, A1, A2, A3](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3]
): ZStream[R1 & R2 & R3, E1 | E2 | E3, (A1, A2, A3)] =
  combine(p1, p2).zipLatestWith(p3) {
    case ((a1, a2), a3) ⇒ (a1, a2, a3)
  }

def combine[R1, R2, R3, R4, E1, E2, E3, E4, A1, A2, A3, A4](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3],
  p4: ZStream[R4, E4, A4],
): ZStream[
  R1 & R2 & R3 & R4,
  E1 | E2 | E3 | E4,
  (A1, A2, A3, A4)
] = combine(p1, p2, p3).zipLatestWith(p4) {
  case ((a1, a2, a3), a4) ⇒
    (a1, a2, a3, a4)
}

def combine[
  R1, R2, R3, R4, R5,
  E1, E2, E3, E4, E5,
  A1, A2, A3, A4, A5
](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3],
  p4: ZStream[R4, E4, A4],
  p5: ZStream[R5, E5, A5],
): ZStream[
  R1 & R2 & R3 & R4 & R5,
  E1 | E2 | E3 | E4 | E5,
  (A1, A2, A3, A4, A5)
] = combine(p1, p2, p3, p4).zipLatestWith(p5) {
  case ((a1, a2, a3, a4), a5) ⇒
    (a1, a2, a3, a4, a5)
}

def combine[
  R1, R2, R3, R4, R5, R6,
  E1, E2, E3, E4, E5, E6,
  A1, A2, A3, A4, A5, A6
](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3],
  p4: ZStream[R4, E4, A4],
  p5: ZStream[R5, E5, A5],
  p6: ZStream[R6, E6, A6],
): ZStream[
  R1 & R2 & R3 & R4 & R5 & R6,
  E1 | E2 | E3 | E4 | E5 | E6,
  (A1, A2, A3, A4, A5, A6)
] = combine(p1, p2, p3, p4, p5).zipLatestWith(p6) {
  case ((a1, a2, a3, a4, a5), a6) ⇒
    (a1, a2, a3, a4, a5, a6)
}

def combine[
  R1, R2, R3, R4, R5, R6, R7,
  E1, E2, E3, E4, E5, E6, E7,
  A1, A2, A3, A4, A5, A6, A7
](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3],
  p4: ZStream[R4, E4, A4],
  p5: ZStream[R5, E5, A5],
  p6: ZStream[R6, E6, A6],
  p7: ZStream[R7, E7, A7],
): ZStream[
  R1 & R2 & R3 & R4 & R5 & R6 & R7,
  E1 | E2 | E3 | E4 | E5 | E6 | E7,
  (A1, A2, A3, A4, A5, A6, A7)
] = combine(p1, p2, p3, p4, p5, p6).zipLatestWith(p7) {
  case ((a1, a2, a3, a4, a5, a6), a7) ⇒
    (a1, a2, a3, a4, a5, a6, a7)
}

def combine[
  R1, R2, R3, R4, R5, R6, R7, R8,
  E1, E2, E3, E4, E5, E6, E7, E8,
  A1, A2, A3, A4, A5, A6, A7, A8
](
  p1: ZStream[R1, E1, A1],
  p2: ZStream[R2, E2, A2],
  p3: ZStream[R3, E3, A3],
  p4: ZStream[R4, E4, A4],
  p5: ZStream[R5, E5, A5],
  p6: ZStream[R6, E6, A6],
  p7: ZStream[R7, E7, A7],
  p8: ZStream[R8, E8, A8],
): ZStream[
  R1 & R2 & R3 & R4 & R5 & R6 & R7 & R8,
  E1 | E2 | E3 | E4 | E5 | E6 | E7 | E8,
  (A1, A2, A3, A4, A5, A6, A7, A8)
] = combine(p1, p2, p3, p4, p5, p6, p7).zipLatestWith(p8) {
  case ((a1, a2, a3, a4, a5, a6, a7), a8) ⇒
    (a1, a2, a3, a4, a5, a6, a7, a8)
}